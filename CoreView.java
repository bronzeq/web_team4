package core;

import java.util.ArrayList;
import java.util.Scanner;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import calender.MainPromptCalender;
import hotel_reservation.HotelReservationControl;
import hotels.HotelControl;
import plane_reservation.PlaneReservationControl;
import planes.PlaneControl;
import users.UsersControl;

public class CoreView {
	// CoreView
	// 메인 화면이 여기로 오게 됩니다
	UsersControl usersControl = new UsersControl();
	PlaneReservationControl planeReservationControl = new PlaneReservationControl();
	PlaneControl planeControl = new PlaneControl();
	HotelReservationControl hotelReservationControl = new HotelReservationControl();
	HotelControl hotelControl = new HotelControl();
	MainPromptCalender mainPromptCalender = new MainPromptCalender();
	

	public static String session_id;

	public void view() {Scanner sc = new Scanner(System.in);
    String title = "ABO 여행사\n";
    String menu = "1. 회원가입\n2. 로그인\n3. 비회원 예매하기\n4. 종료";
    String menu_1 = "1. 마이페이지\n2. 예매하기\n3. 로그아웃";
    String menu_1_1 = "1.예약 목록\n2.예약 수정\n3.회원 정보 수정 "; // 마이페이지 메뉴
    String[] arMenu_1_2 = {"Korea", "Japan", "America", "China" };
    String[] arMenu_1_2_1 = { "편도", "왕복" };
    String[] arMenu_1_2_2 = { "A호텔","B호텔" };
    String[] ar_reservation_chioce = {"항공권예약","호텔예약"};
    String[] ar_planeclass_choice = {"first_c", "business_c","economy_c"}; 
    int choice = 0, trip_choice = 0,  round_choice = 0, login_choice = 0;
    int departure_choice = 0, arrival_choice = 0, city_choice = 0;
    String plane_num1 = "", plane_num2 = "", hotal_num = "";
    //mypage 선택 사항 변수
    int mypage_choice=0;
    ArrayList<String> user_choice = new ArrayList<String>();
    boolean id_check = true;
    // login 여부 확인
    boolean login_flag = false;
 
    String id = "";
    String pw = "";
    String name = "";
    String phone = "";
    String email = "";
    String air ="";
    String depart="";
    String arrive="";
    int point = 0;
    int guest = 0;
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
          if (usersControl.check_dup_id(id) == true) {
             System.out.println("중복된 아이디 입니다.");
             while (id_check) {
                System.out.print("1. 아이디 다시 입력하기\n2. 나가기\n");
                login_choice = sc.nextInt();
                switch (login_choice) {
                case 1:
                   System.out.println("아이디를 입력하세요.");
                   id = sc.next();
                   if (usersControl.check_dup_id(id) == false) {
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
             if (usersControl.register(id, pw, name, phone, email, point)) {
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
          String new_phone="";
          String new_pw="";
          System.out.println("========로그인=======");
          System.out.print("아이디를 입력하세요 : ");
          id = sc.next();
          System.out.print("비밀번호를 입력하세요 : ");
          pw = sc.next();

          try {
             if (usersControl.check_dup_id(id)) {
                if (usersControl.login(id, pw)) {
                   System.out.println("로그인 성공");
                   //로그인 성공시 마이페이지 접근 가능!! 
                   login_flag = true;
                   System.out.println(menu_1);
                   choice = sc.nextInt();
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
          
          switch (choice) {
          // 마이페이지 영역
          case 1:
             String new_PW="",new_Phone="";
             System.out.println(menu_1_1);
             //사용자에게 마이페이지 이용 선택지보여주고 선택하게 하는 것.
             mypage_choice = sc.nextInt();
             int updatemember_choice=0;
             int upre_choice=0;
             //flag = Update_member가 boolean 값이니 넣어주기 
             boolean flag = false;
             switch(mypage_choice) {
             case 1:
                System.out.println("SelectAll() 예약 목록 확인");
//                   System.out.println(usersControl.SelectAll(session_id,air,depart,arrive,guest));
                //오류부분 수정바람
                break;
             case 2:
                
                System.out.println("예약 수정");
                //String pw,String air,String depart, String arrive,String guest
                System.out.println("1.출발지 수정\n2.도착지 수정\n3.인원수 수정");
                
                System.out.println("Update_reservation() 예약 수정");
                break;
             case 3:
                System.out.println("==========회원정보 수정==========");
                System.out.println("1.비밀번호 수정\n2.핸드폰번호 수정");
                updatemember_choice=sc.nextInt();
                if(updatemember_choice==1) {
                   System.out.println("변경하실 비밀번호를 입력하세요 : ");
                   new_PW=sc.next();
                   //String pw,String new_pw,String Phone,String new_phone
                   //new_phone자리에 그냥 원래 phone 변수 넣어주기 > 비밀번호만 바꾸니까. 아무것도 안넣어주면 null값이 된다. 
                   flag = usersControl.Update_member(pw,new_PW,phone,phone);
                      System.out.println("비밀번호 수정 완료");         
                   }
                else if(updatemember_choice==2) {
                   System.out.println("변경하실 핸드폰 번호를 입력하세요 : ");
                   new_Phone = sc.next();
                   flag = usersControl.Update_member(pw,pw,phone,new_Phone);
                   System.out.println("핸드폰 번호 수정 완료");
                }
                break;
             
             }
          
             break;
          // 예매하기 영역
          case 2:
			//항공권or호텔
        	for (int i = 0; i < ar_reservation_chioce.length; i++) {
				System.out.println(i + 1 + ar_reservation_chioce[i]);
			}
			choice = sc.nextInt();
			
			switch (choice) {
			// 항공권 예약
			case 1:
				//예약번호
				planeReservationControl.start_plane_reservation();
				
				//출발지
				System.out.println("[출발지]");
				for (int i = 0; i < arMenu_1_2.length; i++) {
					System.out.println((i + 1) + arMenu_1_2[i]);
				}
				//인덱스로 접근하기 위해 -1
				departure_choice = sc.nextInt() - 1;
				
				//도착지
				System.out.println("[도착지]");
				for (int i = 0; i < arMenu_1_2.length; i++) {
					System.out.println((i + 1) + arMenu_1_2[i]);
				}
				//인덱스로 접근하기 위해 -1
				arrival_choice = sc.nextInt() - 1;
				
				System.out.println("1. 편도\n2. 왕복\n");
				trip_choice = sc.nextInt();
				// UP

				switch (trip_choice) {
				// 편도 영역
				case 1:
//					★달력 출력메소드;
					//출발지, 도착지, 선택날짜, 선택시간 넘겨줌
					//airplaneList()에서 해당 조건에 맞는 planeList 출력
					String userDate = mainPromptCalender.runPROMPT();
					System.out.print("시간을 입력하세요 : ");
					int userTime = sc.nextInt();
					planeControl.airplaneList(arMenu_1_2[departure_choice], arMenu_1_2[arrival_choice], userDate, userTime);
					// 리스트에서 비행기번호 입력
					//여기부터 while 문으로 감싸야 할듯? => 중복검사 때문에
					System.out.println("비행기 번호 입력 : ");
					plane_num1 = sc.next();
					
					if(planeReservationControl.check_plane_num(plane_num1)){
						//비행기 클래스 선택
						System.out.println("[비행기 클래스 선택]");
						for (int i = 0; i < ar_planeclass_choice.length; i++) {
							System.out.println((i+1)+ar_planeclass_choice[i]);
						}
						String grade = ar_planeclass_choice[sc.nextInt()-1];

						//등급별 가격 출력
						planeControl.price(plane_num1, grade);
						
						//성인, 소아, 유아 인원수 입력
						int seat_count = planeReservationControl.setSeatCount();
						planeReservationControl.setSeatTotal(plane_num1, null, grade, seat_count);
						//예약내용 보여주기 => 왕복영역도 동일하게 사용하기 때문에 메소드로 만들면 될것 같음
//						System.out.println("결제하시겠습니까? (y/n)");
//						String finish = sc.next();
						
//						if(finish == "y") {
//							//로그인여부 확인
							//로그인o - 결제 진행
//							//로그인x - 로그인창 or 비회원 로그인
//						}else if((finish == "n")) {
//							//메인으로 돌아가기
//						}else {
//							System.out.println("잘못된 입력입니다. (y/n)로 입력하세요");
//						}
						
						System.out.print(planeReservationControl.total_price()+"원.");
					}else {
						System.out.println("잘못.");
					}

					break;
					
				// 왕복 영역
				case 2:
//					★달력 출력메소드;
					//달력 추가하고 갑사 받아오면 오류 없어짐
					userDate =  mainPromptCalender.runPROMPT();
					System.out.print("시간을 입력하세요 : ");
					userTime = sc.nextInt();
					planeControl.airplaneList(arMenu_1_2[departure_choice], arMenu_1_2[arrival_choice], userDate, userTime);
					// 리스트에서 비행기번호 입력
					System.out.println("비행기 번호 입력 : ");
					plane_num1 = sc.next();
					
//					★달력 출력메소드;
					//위의 출발지와 도착지를 바꿔줌
					userDate =  mainPromptCalender.runPROMPT();
					System.out.print("시간을 입력하세요 : ");
					userTime = sc.nextInt();
					planeControl.airplaneList(arMenu_1_2[arrival_choice], arMenu_1_2[departure_choice], userDate, userTime);
					// 리스트에서 비행기번호 입력
					System.out.println("비행기 번호 입력 : ");
					plane_num2 = sc.next();
					
					if(planeReservationControl.check_plane_num(plane_num1) || planeReservationControl.check_plane_num(plane_num2)){
						//비행기 클래스 선택
						System.out.println("[비행기 클래스 선택]");
						for (int i = 0; i < ar_planeclass_choice.length; i++) {
							System.out.println((i+1)+ar_planeclass_choice[i]);
						}
						String grade = ar_planeclass_choice[sc.nextInt()-1];
						
						//등급별 가격 출력
						planeControl.price(plane_num1, grade);
						
						//성인, 소아, 유아 인원수 입력
						int seat_count = planeReservationControl.setSeatCount();
						planeReservationControl.setSeatTotal(plane_num1, plane_num2, grade, seat_count);
						//예약내용 보여주기
//						System.out.println("결제하시겠습니까? (y/n)");
//						String finish = sc.next();
						
//						if(finish == "y") {
//							//로그인여부 확인
							//로그인o - 결제 진행
//							//로그인x - 로그인창 or 비회원 로그인
//						}else if((finish == "n")) {
//							//메인으로 돌아가기
//						}else {
//							System.out.println("잘못된 입력입니다. (y/n)로 입력하세요");
//						}
						
						System.out.print(planeReservationControl.total_price()+"원.");
					}else {
						System.out.println("잘못된 입력입니다.");
					}
					break;
					
				// 뒤로가기(보류)	
				case 3:
					break;
				}
				
			//호텔영역
			case 2:
				hotelReservationControl.start_hotel_reservation();
				
				System.out.println("[여행지]");
				for (int i = 0; i < arMenu_1_2.length; i++) {
					System.out.println((i + 1) + arMenu_1_2[i]);
				}
				//인덱스로 접근하기 위해서 -1
				city_choice = sc.nextInt() - 1;
				
				//입실일, 퇴실일 입력
//				★달력 출력메소드
				
				//여행지에 맞는 호텔리스트 출력(여행지,입실일 )
//				hotelControl.hotelList(city_choice, userDate);
				System.out.println("호텔 번호 입력 : ");
				hotal_num = sc.next();
				
//				★호텔번호 중복검사 메소드
//				if(★호텔번호 중복검사 메소드) {
				
//					★해당 호텔번호의 가격출력 메소드
					//룸(1,2,4)선택
					hotelReservationControl.setRoomCount();
					//room_total 바꾸는 메소드
					hotelReservationControl.setRoomTotal();
					//예약내용 보여주기
//					System.out.println("결제하시겠습니까? (y/n)");
//					String finish = sc.next();
					
//					if(finish == "y") {
//						//로그인여부 확인
						//로그인o - 결제 진행
//						//로그인x - 로그인창 or 비회원 로그인
//					}else if((finish == "n")) {
//						//메인으로 돌아가기
//					}else {
//						System.out.println("잘못된 입력입니다. (y/n)로 입력하세요");
//					}
					System.out.print(hotelReservationControl.hotel_total_price()+"원.");
//				}else{
//					System.out.println("잘못된 입력입니다.");
//				}
				
				break;
			

			}
		
          // 로그아웃 영역
          case 3:
        	  usersControl.logout();
             System.out.println("logout 되었습니다. ");
             break;
          }
          break;
       

          }
       }
    }

 }

