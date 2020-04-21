package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import vo.TourDTO;

public class TourDAO {
	// 달력출력 메소드

	// 결제 메소드(회원, 비회원)
	// 나라, 편도 왕복, 호텔
	public void pay(ArrayList<Object> user_choice, TourDTO user, int idx) {
		if (user == null) {
			// 로그인 메소드

		}

		if (user_choice.get(1) == null) {

		}

	}

	// 로그인 메소드()

	// 중복확인 메소드
	public boolean check_dup_id(String id) {
		String str = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// localaddress 자리에 ip 넣기
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521", "hr", "hr");
			Statement state = conn.createStatement();
			state.execute("SELECT COUNT(*) FROM REGISTER WHERE ID IN ('" + id + "')");
			ResultSet resultSet = state.executeQuery("SELECT COUNT(*) FROM REGISTER WHERE ID IN ('" + id + "')");
			if (resultSet.next()) {
				str = resultSet.getString(1);
			}
			if (str.equals("0")) {
				return false;
			}
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}

	// 회원가입 TourDTO(아이디, 비밀번호, 이름, 전화번호, 이메일)
	public int register(TourDTO dto) throws AddressException, MessagingException {
		int result = 0;
		Random rand = new Random();
		Scanner sc = new Scanner(System.in);
		int code = (int) (rand.nextFloat() * 10000);
		sendMail(dto, code);
		System.out.print("코드를 입력하세요 : ");
		if (sc.nextInt() == code) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				// localaddress 자리에 ip 넣기
				Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521", "hr", "hr");
				Statement state = conn.createStatement();
				result = state.executeUpdate("INSERT INTO REGISTER" 
						+ "(id, pw, name, phone, email) VALUES" + " ('"
						+ dto.getId() + "', '" + dto.getPw() + "', '" 
						+ dto.getName() + "', '" + dto.getPhone() + "', '"
						+ dto.getEmail() + "')");
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 메일 발송 메소드
	public void sendMail(TourDTO dto, int code) throws AddressException, MessagingException {
		String host = "smtp.naver.com";

		// 보내는 사람 이메일 주소(@naver.com)제외, 비밀번호
		final String id = "yhya0904";
		final String pw = "leeheader7679!";
		int port = 465;

		String recipient = dto.getEmail();
		String subject = "메일 발송 확인";
		int body = code;

		Properties props = System.getProperties();

		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", host);

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			String userName = id;
			String passWord = pw;

			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(userName, passWord);
			}
		});
		session.setDebug(true);

		Message mimeMessage = new MimeMessage(session);
		// internetAddress 안에 보내는 사람 이메일 주소 넣기 (@naver.com) 포함
		mimeMessage.setFrom(new InternetAddress("yhya0904@naver.com"));
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

		mimeMessage.setSubject(subject);
		mimeMessage.setText(Integer.toString(body));
		Transport.send(mimeMessage);
	}

	public void view() {

		Scanner sc = new Scanner(System.in);
		String title = "ABO 여행사\n";
		String menu = "1. 회원가입\n2. 로그인\n3. 비회원 예매하기\n4. 종료";
		String menu_1 = "1. 마이페이지\n2. 예매하기\n3. 로그아웃";
		String menu_1_1 = ""; // 마이페이지 메뉴
		String menu_1_2 = "1. 일본\n2. 미국\n3. 중국";
		String[] arMenu_1_2 = { "일본", "미국", "중국" };
		String menu_1_2_1 = "1. 편도\n2. 왕복";
		String[] arMenu_1_2_1 = { "편도", "왕복" };
		String menu_1_2_2 = "1. A호텔\n2. B호텔";
		String[] arMenu_1_2_2 = { "A호텔" };
		int choice = 0, country_choice = 0, trip_choice = 0, hotel_choice = 0, round_choice = 0, login_choice = 0;
		ArrayList<String> user_choice = new ArrayList<String>();
		boolean id_check = true;

		while (true) {
			System.out.println(title + menu);
			choice = sc.nextInt();

			// 종료
			if (choice == 4) {
				break;
			}

			switch (choice) {
			// 회원가입 영역
			case 1:
				TourDTO dto = new TourDTO();

				//ID입력
				System.out.println("아이디를 입력하세요.");
				dto.setId(sc.next());
				if(check_dup_id(dto.getId()) == true) {
					System.out.println("중복된 아이디 입니다.");
					while (id_check) {
						System.out.print("1. 계속\n2. 나가기\n");
						login_choice = sc.nextInt();
						switch (login_choice) {
							case 1:
								System.out.println("아이디를 입력하세요.");
								dto.setId(sc.next());
								if(check_dup_id(dto.getId()) == false) {
									id_check = false;
									break;
								}
								System.out.println("중복된 아이디 입니다.");
								break;
							case 2:
								view();
						}
					}
				}
				id_check = true;
				
				
				System.out.println("비밀번호를 입력하세요.");
				dto.setPw(sc.next());
				System.out.println("이름을 입력하세요.");
				dto.setName(sc.next());
				System.out.println("번호를 입력하세요.");
				dto.setPhone(sc.next());
				System.out.println("이메일을 입력하세요.");
				dto.setEmail(sc.next());

				try {
					if (new TourDAO().register(dto) == 1) {
						System.out.println("회원가입 성공");
					} else {
						System.out.println("회원가입 실패");
					}
				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}

				break;
			// 로그인 영역

			case 2:
				// "1. 마이페이지\n2. 예매하기\n3. 로그아웃"
				System.out.println(menu_1);
				choice = sc.nextInt();

				switch (choice) {
				// 마이페이지 영역
				case 1:
					System.out.println("들어옴");
					break;
				// 예매하기 영역
				case 2:
//					"1. 일본\n2. 미국\n3. 중국"
					System.out.println(menu_1_2);
					country_choice = sc.nextInt();
					user_choice.add(arMenu_1_2[country_choice - 1]);
					System.out.println("1.항공기 예약\n2.호텔 예약");
					trip_choice = sc.nextInt();

					switch (trip_choice) {
					// 항공권 예약 영역
					case 1:
//						"1. 편도\n2. 왕복\n"
						System.out.println(menu_1_2_1);
						trip_choice = sc.nextInt(); // 보류
						user_choice.add(arMenu_1_2_1[trip_choice - 1]);

						switch (trip_choice) {
						// 편도 영역
						case 1:
//							달력 출력(메소드)
							break;
						// 왕복 영역
						case 2:
//							달력 출력(메소드)
							break;
						// 뒤로가기(보류)
						case 3:
							break;
						}

						break;
					// 호텔예약 영역
					case 2:
						System.out.println(menu_1_2_2);
						hotel_choice = sc.nextInt();
						user_choice.add(arMenu_1_2_2[hotel_choice - 1]);
						switch (hotel_choice) {
						// A 호텔
						case 1:
//						달력 출력(메소드)
							break;
						// B호텔
						case 2:
//						달력 출력(메소드)
							break;
						// 뒤로가기
						case 3:
							break;
						}
						break;
					// 뒤로가기 영역(보류)
					case 3:
						break;
					}
					break;
				// 로그아웃 영역
				case 3:
					break;
				}
				break;
			// 비회원 예매하기 영역
			case 3:
//				"1. 일본\n2. 미국\n3. 중국"
				System.out.println(menu_1_2);
				country_choice = sc.nextInt();

				System.out.println("1.항공기 예약\n2.호텔 예약");
				trip_choice = sc.nextInt();
				switch (trip_choice) {
				// 항공권 예약 영역
				case 1:
//					"1. 편도\n2. 왕복\n"
					System.out.println(menu_1_2_1);
					round_choice = sc.nextInt(); // 보류

					switch (round_choice) {
					// 편도 영역
					case 1:
//						달력 출력(메소드)
						break;
					// 왕복 영역
					case 2:
//						달력 출력(메소드)
						break;
					// 뒤로가기(보류)
					case 3:
						break;
					}

					break;
				// 호텔예약 영역
				case 2:
					System.out.println(menu_1_2_2);
					hotel_choice = sc.nextInt();
					switch (hotel_choice) {
					// A 호텔
					case 1:
//					달력 출력(메소드)
						break;
					// B호텔
					case 2:
//					달력 출력(메소드)
						break;
					// 뒤로가기
					case 3:
						break;
					}
					break;
				default:

				}
			}
		}

	}

}