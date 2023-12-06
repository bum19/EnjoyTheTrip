package com.ssafy.web.member.model.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.web.exception.ExceptionCode;
import com.ssafy.web.exception.MyException;
import com.ssafy.web.member.model.LoginCheckDto;
import com.ssafy.web.member.model.MemberDto;
import com.ssafy.web.member.model.MemberSecDto;
import com.ssafy.web.member.model.mapper.MemberMapper;
import com.ssafy.web.member.model.service.MemberService;
import com.ssafy.web.member.sec.mapper.MemberSecMapper;

@Service
public class MemberServiceImpl implements MemberService {

//	@Autowired
//	private SqlSession sqlSession;

	private MemberMapper memberMapper;
	private MemberSecMapper memberSecMapper;

	@Autowired
	public MemberServiceImpl(MemberMapper memberMapper, MemberSecMapper memberSecMapper) {
		super();
		this.memberMapper = memberMapper;
		this.memberSecMapper = memberSecMapper;
	}

	@Override
	public int emailCheck(String email) throws Exception {
//		return sqlSession.getMapper(MemberMapper.class).idCheck(userId);
		return memberMapper.emailCheck(email);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void joinMember(MemberDto memberDto) throws Exception {
		// 이미 가입된 회원인지 검증
		MemberSecDto memberSecDto = memberSecMapper.find(memberDto.getEmail());
		if (memberSecDto != null) {
			throw new MyException(ExceptionCode.EMAIL_EXIST);
		} else {
			memberSecDto = new MemberSecDto();
			// 패스워드 SHA-256 해싱.
			String userPassword = memberDto.getUserPassword();
			String salt = UUID.randomUUID().toString();
			userPassword = encodeSha256(userPassword, salt);
			memberDto.setUserPassword(userPassword);

			// 비밀번호찾기 answer AES 암호화
			String number = memberDto.getAnswer();
			byte[] key = generateKey("AES", 128);
			String[] res = aesEncrypt(number, key);
			memberDto.setAnswer(res[0]);

			// 가입할 회원에 대응되는 info정보.
			memberSecDto.setEmail(memberDto.getEmail());
			memberSecDto.setSalt(salt);
			memberSecDto.setSecKey(byteArrayToHex(key));
			memberSecDto.setInitVector(res[1]);
			memberMapper.joinMember(memberDto);
			memberSecMapper.join(memberSecDto);
		}
	}

	@Override
	public MemberDto loginMember(MemberDto memberDto) throws Exception {
		MemberSecDto memberSecDto = memberSecMapper.find(memberDto.getEmail());
		if (memberSecDto == null) { // 아이디가 없음
			return null;
		} else { // 아이디가 있음 -> 로그인 시도
			String password = memberDto.getUserPassword();
			password = encodeSha256(password, memberSecDto.getSalt());
			memberDto.setUserPassword(password);
			String email = memberDto.getEmail();
			memberDto = memberMapper.loginMember(memberDto);
			if (memberDto != null) { // 로그인 성공
				memberMapper.deleteLoginCheck(email);
				return memberDto;
			} else { // 로그인 실패
				memberMapper.plusLoginCount(email);
				return null;
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public MemberDto getMember(String email) throws Exception {
		MemberDto memberDto = memberMapper.getMember(email);
		MemberSecDto memberSecDto = memberSecMapper.find(email);

		String answer = memberDto.getAnswer();
		String secKey = memberSecDto.getSecKey();
		String iv = memberSecDto.getInitVector();

		answer = aesDecrypt(answer, hexToByteArray(secKey), iv);
		System.out.println(answer);
		memberDto.setAnswer(answer);

		return memberDto;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateMember(MemberDto memberDto) throws Exception {
		if (memberDto.getAnswer() != null) {
			// 비밀번호찾기 answer AES 암호화
			String number = memberDto.getAnswer();
			byte[] key = generateKey("AES", 128);
			String[] res = aesEncrypt(number, key);
			memberDto.setAnswer(res[0]);
			MemberSecDto memberSecDto = memberSecMapper.find(memberDto.getEmail());
			memberSecDto.setSecKey(byteArrayToHex(key));
			memberSecDto.setInitVector(res[1]);

			memberSecMapper.update(memberSecDto);
		}
		memberMapper.updateMember(memberDto);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteMember(String email) throws Exception {
		memberMapper.deleteMember(email);
		memberSecMapper.delete(email);
	}

	@Override
	public String getQuestion(String email) {
		return memberMapper.getQuestion(email);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updatePW(MemberDto memberDto) throws Exception {
		// db에 저장된 답변과 비교
		String answer = memberDto.getAnswer();
		String dbAnswer = memberMapper.getMember(memberDto.getEmail()).getAnswer();
		MemberSecDto memberSecDto = memberSecMapper.find(memberDto.getEmail());
		if (answer
				.equals(aesDecrypt(dbAnswer, hexToByteArray(memberSecDto.getSecKey()), memberSecDto.getInitVector()))) {
			System.out.println("d여기서문제지?");
			// 비밀번호 재설정 정상적으로 진행
			String userPassword = memberDto.getUserPassword();
			String salt = UUID.randomUUID().toString();
			userPassword = encodeSha256(userPassword, salt);
			memberDto.setUserPassword(userPassword);
			memberMapper.updatePW(memberDto);

			// 패스워드 해싱정보 업데이트
			memberSecDto.setSalt(salt);
			memberSecMapper.update(memberSecDto);
			return true;
		} else { // 답 틀렸으면
			return false;
		}
	}

	/* ADMIN */
	@Override
	public List<MemberDto> listMember(Map<String, Object> map) throws Exception {
		return memberMapper.listMember(map);
	}

	public String encodeSha256(String source, String msalt) throws NoSuchAlgorithmException {
		String result = "";
		byte[] a = source.getBytes();
		byte[] salt = msalt.getBytes();
		byte[] bytes = new byte[a.length + salt.length];

		System.arraycopy(a, 0, bytes, 0, a.length);
		System.arraycopy(salt, 0, bytes, a.length, salt.length);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(bytes);

		byte[] byteData = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
		}

		result = sb.toString();
		return result;
	}

	@Override
	public boolean loginCheck(String email) throws Exception {
		LoginCheckDto loginCheckDto = memberMapper.loginCheck(email);
		if (loginCheckDto != null) { // 로그인 시도가 있었는지
			Date now = new Date();
			Date thirtyMinutesBefore = new Date(now.getTime() - (30 * 60 * 1000));
			int result = loginCheckDto.getLastTryTime().compareTo(thirtyMinutesBefore);

			if (loginCheckDto.getTryCount() == 5) { // 시도 횟수가 5인 경우
				if (result < 0) { // 30분이 지났다
					// login_check 테이블에 해당 레코드 삭제
					memberMapper.deleteLoginCheck(loginCheckDto.getEmail());
					return true;
				} else { // 30분이 아직 안지났다
					return false;
				}
			} else { // 시도 횟수가 5 이하인 경우
				if (result < 0) { // 30분이 지났다
					// login_check 테이블에 해당 레코드 삭제
					memberMapper.deleteLoginCheck(loginCheckDto.getEmail());
				}
				return true;
			}
		} else {
			return true;
		}
	}

	/* 비밀번호찾기 답 AES 암호화 */
	public static String[] aesEncrypt(String msg, byte[] key) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		String iv = "AAAAAAAAAAAAAAAA";
		SecureRandom random = new SecureRandom();
		byte[] iv = new byte[16];
		random.nextBytes(iv);
		System.out.println("만들어질때 길이 16아님? :" + iv.length);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv));
		byte[] encrypted = cipher.doFinal(msg.getBytes());

		return new String[] { byteArrayToHex(encrypted), byteArrayToHex(iv) };
	}

	public static String aesDecrypt(String msg, byte[] key, String iv) throws Exception {
		System.out.println("이건 길이 몇이니  : " + hexToByteArray(iv).length);
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(hexToByteArray(iv)));

		System.out.println("여기전에 문제생기는게맞음?");
		byte[] encrypted = hexToByteArray(msg);
		byte[] original = cipher.doFinal(encrypted);

		return new String(original);
	}

	public static byte[] generateKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		keyGenerator.init(keySize);
		SecretKey key = keyGenerator.generateKey();
		return key.getEncoded();
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	public static String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}
}
