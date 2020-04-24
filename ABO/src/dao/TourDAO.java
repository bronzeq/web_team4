package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	Connection conn;
	PreparedStatement pstm;
	ResultSet rs;
	TourDTO dto = null;

	private final int KEY = 7;

	// 달력출력 메소드

	// 항공기 출력 메소드
	public void airplaneList(String userDate) {
		String query = "";
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH");
		// time1 == user가 선택한 시간
		// time2 == 현재시간
		Date time = new Date();
		String time1 = format1.format(userDate);
		String time2 = format1.format(time);

		int compare = time1.compareTo(time2);
		if (compare > 0) {
			// 선택한 날이 현재 날보다 이후일 때
			query = "SELECT * FROM AIRPLANE WHERE DATE = \'" + userDate + "\'";
			showAirplaneList(query);
		} else if (compare < 0) {
			// 선택한 날이 현재 날보다 이전일때
			System.out.println(time2 + " 이전의 날을 입력했습니다.");
		} else {
			// 선택한 날이 현재 날과 같을 때
			query = "";
			showAirplaneList(query);
		}

	}

	// 항공기 리스트 출력
	public void showAirplaneList(String query) {
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
			System.out.println("-----------------------------------------------------------------------");
			// column명 쓰기
			System.out.println();
			while (rs.next()) {
				System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
						+ rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getInt(6) + "\t" + rs.getString(7) + "\t"
						+ rs.getInt(8));
			}
			System.out.println("-----------------------------------------------------------------------");
			pstm.close();
		} catch (SQLException sqle) {
			System.out.println(sqle);
			System.out.println("showAirplaneList() 쿼리문 오류");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("알 수 없는 오류(showAirplaneList())");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	// 호텔 리스트 출력
	public void hotelList(Date userDate) {
		String query = "";
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		// time1 == user가 선택한 시간
		// time2 == 현재시간
		Date time = new Date();
		String time1 = format1.format(userDate);
		String time2 = format1.format(time);

		int compare = time1.compareTo(time2);
		if (compare < 0) {
			// 선택한 날이 현재 날보다 이전일때
			System.out.println(time2 + " 이전의 날을 입력했습니다.");
		} else {
			// 선택한 날이 현재 날과 같거나 이후일 때
			query = "SELECT * FROM HOTEL WHERE DATE = \'" + userDate + "\'";
			showAirplaneList(query);
		}

	}

	// 호텔 리스트 출력
	public void showHotelList(String query) {
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
			System.out.println("-----------------------------------------------------------------------");
			// column명 쓰기
			System.out.println();
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getString(4));
			}
			System.out.println("-----------------------------------------------------------------------");
			pstm.close();
		} catch (SQLException sqle) {
			System.out.println(sqle);
			System.out.println("showHotelList() 쿼리문 오류");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("알 수 없는 오류(showHotelList())");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

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
	public boolean login(String id, String pw) {
		boolean check = false;
		String query = "SELECT COUNT(*) FROM REGISTER" + " WHERE ID = ? AND PW = ?";
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, id);
			pstm.setString(2, encrypt(pw));
			rs = pstm.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) == 1) {
					check = true;
				}
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
			System.out.println("login() 쿼리문 오류");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("그밖의 문제(login())");
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}

		return check;

	}

	// 아이디 중복확인 메소드
	public boolean check_dup_id(String id) {
		String query = "SELECT COUNT(*) FROM REGISTER WHERE ID = ?";
		boolean check = true;
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, id);
			rs = pstm.executeQuery();
			rs.next();
			if (rs.getInt(1) == 0) {
				check = false;
			}
			pstm.close();
		} catch (SQLException sqle) {
			System.out.println(sqle);
			System.out.println("check_dup_id() 쿼리문 오류");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("알 수 없는 오류(check_dup_id() 메소드)");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return check;
	}

	// 회원가입 TourDTO(아이디, 비밀번호, 이름, 전화번호, 이메일)
	public boolean register(String id, String pw, String name, String phone, String email) throws AddressException, MessagingException {
		boolean result = false;
		String query = "";
		Random rand = new Random();
		Scanner sc = new Scanner(System.in);
		int code = (int) (rand.nextFloat() * 10000);
		sendMail(email, code);
		System.out.print("코드를 입력하세요 : ");
		if (sc.nextInt() == code) {
			query = "INSERT INTO REGISTER" + "(id, pw, name, phone, email) " + "VALUES(?, ?, ?, ?, ?)";
			try {
				int idx = 0;
				conn = DBConnection.getConnection();
				pstm = conn.prepareStatement(query);
				pstm.setString(++idx, id);
				pstm.setString(++idx, encrypt(pw));
				pstm.setString(++idx, name);
				pstm.setString(++idx, phone);
				pstm.setString(++idx, email);
				pstm.executeQuery();
				pstm.close();
				result = true;
			} catch (SQLException sqle) {
				System.out.println(sqle);
				System.out.println("register() 쿼리문 오류");
			} catch (Exception e) {
				System.out.println(e);
				System.out.println("알 수 없는 오류(register())");
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstm != null) {
						pstm.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		return result;
	}

	// 메일 발송 메소드
	public void sendMail(String email, int code) throws AddressException, MessagingException {
		String host = "smtp.naver.com";

		// --- 안에 보내는 사람 이메일 주소(@naver.com)제외, 비밀번호
		final String id = "yhya0904";
		final String pw = "leeheader7679!";
		int port = 465;

		String recipient = email;
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
		// --- 안에 보내는 사람 이메일 주소 넣기 (@naver.com) 포함
		mimeMessage.setFrom(new InternetAddress("yhya0904@naver.com"));
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		mimeMessage.setSubject(subject);
		mimeMessage.setText(Integer.toString(body));
		Transport.send(mimeMessage);
	}

	public String encrypt(String pw) {
		String en_pw = "";
		for (int i = 0; i < pw.length(); i++) {
			en_pw += (char) (pw.charAt(i) * KEY);
		}
		return en_pw;
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
		// login 여부 확인
		boolean login_flag = false;
		
		String id = "";
		String pw = "";
		String name = "";
		String phone = "";
		String email = "";

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
				// ID입력
				System.out.println("아이디를 입력하세요.");
				id = sc.next();
				if (check_dup_id(id) == true) {
					System.out.println("중복된 아이디 입니다.");
					while (id_check) {
						System.out.print("1. 아이디 다시 입력하기\n2. 나가기\n");
						login_choice = sc.nextInt();
						switch (login_choice) {
						case 1:
							System.out.println("아이디를 입력하세요.");
							id = sc.next();
							if (check_dup_id(id) == false) {
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
				pw = sc.next();
				System.out.println("이름을 입력하세요.");
				name = sc.next();
				System.out.println("번호를 입력하세요.");
				phone = sc.next();
				System.out.println("이메일을 입력하세요.");
				email = sc.next();

				try {
					if (register(id, pw, name, phone, email)) {
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
				System.out.print("아이디를 입력하세요 : ");
				id = sc.next();
				System.out.print("비밀번호를 입력하세요 : ");
				pw = sc.next();

				try {
					if (check_dup_id(id)) {
						if (login(id, pw)) {
							System.out.println("로그인 성공");
							login_flag = true;
						} else {
							System.out.println("비밀번호 오류");
						}
					} else {
						System.out.println("아이디가 없습니다.");
					}
				} catch (Exception e) {
					System.out.println("로그인 오류");
				}

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