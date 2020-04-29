package hotel_reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import core.DBConnection;

public class HotelReservationControl {

	Connection conn;
	PreparedStatement pstm;
	ResultSet rs;
//	String session_hotel_reservation = "";
	public static String session_hotel_reservation = "";
	   
	   public void start_hotel_reservation(String session_id) {
	      Random r = new Random();
	      String temp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	      for (int i = 0; i < 6; i++) {
	         session_hotel_reservation += temp.charAt(r.nextInt(temp.length()));
	      }
	      try {
	          String query = "SELECT COUNT(*) FROM HOTEL_INFORMATION "
	                + "WHERE HOTEL_RESERVATION = ?";
	          conn = DBConnection.getConnection();
	          pstm = conn.prepareStatement(query);
	          pstm.setString(1, session_hotel_reservation);
	          rs = pstm.executeQuery();
	          rs.next();
	          if(rs.getInt(1) == 0) {
	             query = "INSERT INTO HOTEL_INFORMATION"
	                   + " (ID, HOTEL_RESERVATION, HOTEL_NUM, CHECK_IN, CHECK_OUT, ROOM_COUNT, HOTEL_TOTAL_PRICE)"
	                   + "VALUES (?,?,?,?,?,?,?)";
	             pstm = conn.prepareStatement(query);
	             pstm.setString(1, session_id);
	             pstm.setString(2, session_hotel_reservation);
	             pstm.setString(3, null);
	             pstm.setString(4, null);
	             pstm.setString(5, null);
	             pstm.setString(6, null);
	             pstm.setInt(7, 0);
	             pstm.executeQuery();
	          } else {
	             //session_hotel_reservation 코드가 이미 DB에 있을 경우
	             start_hotel_reservation(session_id);
	          }
	       } catch (SQLException sqle) {
	          System.out.println(sqle);
	          System.out.println("start_hotel_reservation() 쿼리문 오류");
	       } catch (Exception e) {
	          System.out.println(e);
	          System.out.println("그밖의 문제(setSeatCount())");
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
	// hotel_info의 seat_count바꾸는 메소드
	public void setRoomCount() {
		int room_1;
		int room_2;
		int room_4;
		String query = "";
		String room_count = "";

		Scanner sc = new Scanner(System.in);
		room_1 = sc.nextInt();
		room_2 = sc.nextInt();
		room_4 = sc.nextInt();

		// DB hotel_info 테이블에 seat_count 전송
		room_count = room_1 + "," + room_2 + "," + room_4;

		try {
			query = "UPDATE HOTEL_INFORMATION SET ROOM_COUNT = ?" + " WHERE ROOM_RESERVATION = ?";
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, room_count);
			pstm.setString(2, session_hotel_reservation);
			rs = pstm.executeQuery();
		} catch (SQLException sqle) {
			System.out.println(sqle);
			System.out.println("setSeatCount() 쿼리문 오류");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("그밖의 문제(setSeatCount())");
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

	// room_total에서 room_count 빼주는 메소드
	public String calRoomTotal(String room_total, String room_count) {
		String[] room_total_ar = room_total.split(",");
		String[] room_count_ar = room_count.split(",");

		int[] arTotal = new int[room_total_ar.length];
		int[] arCount = new int[room_total_ar.length];

		String result = "";

		for (int i = 0; i < room_count_ar.length; i++) {
			arTotal[i] = Integer.parseInt(room_total_ar[i]);
			arCount[i] = Integer.parseInt(room_count_ar[i]);
		}

		for (int i = 0; i < arCount.length; i++) {
			arTotal[i] -= arCount[i];
		}
		result = arTotal[0] + "," + arTotal[1] + "," + arTotal[2];
		return result;
	}

	// hotel의 room_total 바꾸는 메소드
	public void setRoomTotal() {
		String query = "";
		String hotel_num = "";
		String room_total = "";
		String room_count = "";
		try {
			query = "SELECT HOTEL_NUM AND ROOM_COUNT FROM HOTEL_INFORMATION " + "WHERE HOTEL_RESERVATION = ?";
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, session_hotel_reservation);
			rs = pstm.executeQuery();
			if (rs.next()) {
				hotel_num = rs.getString(1);
				room_count = rs.getString(2);
			}
			// hotel_num에 다시 연결
			query = "SELECT SEAT_TOTAL FROM PLANE WHERE PLANE_NUM = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, hotel_num);
			rs = pstm.executeQuery();
			if (rs.next()) {
				// hotel의 구분자로 나뉘어진 room_total 가져옴
				room_total = rs.getString(1);
			}
			// 계산한 뒤 다시 room_total에 담아줌
			room_total = calRoomTotal(room_total, room_count);

			// room_total update
			query = "UPDATE HOTEL SET ROOM_TOTAL = ? WHERE HOTEL_NUM = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, room_total);
			pstm.setString(2, hotel_num);
			pstm.executeQuery();

		} catch (SQLException sqle) {
			System.out.println(sqle);
			System.out.println("setRoomTotal() 쿼리문 오류");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("그밖의 문제(setRoomTotal())");
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
	

//public ArrayList<HotelReservationModel> selectAll(){
//	String query="SELECT * FROM HOTEL_INFORMATION AND GUEST AND HOTEL" + 
//			"WHERE HOTEL_INFORMATION.ID =? AND" + 
//			"HOTEL.HOTEL_NUM=?";
//	
//	//세션 만들고 DB에 넣어주는 작업 필요
//	//세개 조인.
//	
//	
//	ArrayList<HotelReservationModel> users=new ArrayList<>();
//	HotelReservationModel user=null;
//	
//	
//	
//	try {
//		conn=DBConnection.getConnection();
//		pstm=conn.prepareStatement(query);
//		rs=pstm.executeQuery();
//		while(rs.next()) {
//			user=new HotelReservationModel();
//			user.setId(rs.getString(1));
//			user.setHotel_reservation(rs.getString(2));
//			user.setHotel_num(rs.getString(3));
//			user.setCheck_in(rs.getString(4));
//			user.setCheck_out(rs.getString(5));
//			user.setRoom_count(rs.getString(6));
//			user.setHotel_total_price(rs.getInt(7));
//			
//			users.add(user);
//		}
//	} catch(SQLException sqle) {
//		System.out.println("select() 쿼리문 오류");
//	} catch (Exception e) {
//		System.out.println("알 수 없는 오류(select())");
//	} finally {
//		try {
//			if(rs != null) {
//				rs.close();
//			}
//			if(pstm != null) {
//				pstm.close();
//			}
//			if(conn != null) {
//				conn.close();
//			}
//		} catch (SQLException e) {
//			throw new RuntimeException(e.getMessage());
//		}
//	}
//	return users;	
//}
	// 호텔번호 검사 메소드
	public boolean check_hotel_num(String hotel_num) {

		String query = "SELECT HOTEL_NUM FROM HOTEL WHERE HOTEL_NUM = ?";
		boolean check = false;
		try {
			conn = DBConnection.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, hotel_num);
			int result = pstm.executeUpdate();

			if (result == 1) {
				check = true; // 있으면 true
			}

		} catch (SQLException sqle) {
			System.out.println(sqle);
			System.out.println("check_hotel_num() 쿼리문 오류");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("알 수 없는 오류(check_hotel_num())");
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
	
	//구분자로 나눠진 String을 int 배열로 바꾸는 메소드
	   public int[] stringToIntAr(String str) {
	      String[] arTotal = str.split(",");
	      int[] arIntTotal = new int[arTotal.length];
	      for (int i = 0; i < arTotal.length; i++) {
	         arIntTotal[i] = Integer.parseInt(arTotal[i]);
	      }
	      return arIntTotal;
	   }
	   
	   // 호텔 총 가격 리턴하는 메소드
	   public int hotel_total_price() {
	      String query = "";
	      int total_price = 0;
	      int[] room_total_ar = null;
	      String hotal_num = "";
	      
	      try {
	      query = "SELECT HOTEL_NUM AND ROOM_COUNT FROM HOTEL_INFORMATION FROM WHERE HOTEL_RESERVATION = ?";
	      conn = DBConnection.getConnection();
	      pstm = conn.prepareStatement(query);
	      pstm.setString(1, session_hotel_reservation);
	      rs = pstm.executeQuery();
	      
	      if (rs.next()) {
	         // hotel의 구분자로 나뉘어진 room_total 가져옴
	         hotal_num = rs.getString(1);
	         room_total_ar = stringToIntAr(rs.getString(2));
	      }
	      
	      query = "SELECT ROOM_1 AND ROOM_2 AND ROOM_4 FROM HOTEL_CLASS FROM WHERE HOTEL_NUM = ?";
	      pstm = conn.prepareStatement(query);
	      pstm.setString(1, hotal_num);
	      rs = pstm.executeQuery();
	      rs.next();
	         
	      for (int i = 0; i < room_total_ar.length; i++) {
	         total_price += room_total_ar[i]*rs.getInt(i+1);
	      }
	      
	      } catch (SQLException sqle) {
	         System.out.println(sqle);
	         System.out.println("hotel_total_price() 쿼리문 오류");
	      } catch (Exception e) {
	         System.out.println(e);
	         System.out.println("그밖의 문제(hotel_total_price())");
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
	   return total_price;
	}
}

