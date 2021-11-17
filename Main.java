import java.io.*;
import java.sql.*;
public class Main {

	public static void main(String[] args) throws IOException, SQLException{
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		//db 주소 -> 오타 주의
		String url ="jdbc:mysql://localhost:3306/test?useSSL=false";
		//mysql user 닉네임, 비밀번호
		String user ="ino";
		String password = "1234";
		
		// Connection 메서드의 객체 conn 생성 후 
		// DriverManager.getConnection 메서드를 사용해서 위 세 값을 서버에 전송하여 연결
		Connection conn = DriverManager.getConnection(url, user, password);
		
		// PreparedStatement -> sql 구문을 실행하는 연할 (전달)
		PreparedStatement ps = null;
		//sql 구문을 저장할 변수
		String sql = null;
		//ResultSet -> 결과값을 불러오는 메소드
		ResultSet rs = null;
		int n = 0;
		while(true) {
			System.out.println("1.추가, 2.삭제, 3.수정, 4.보기, 5.종료");
			System.out.print("메뉴 번호 입력");
			
			//메뉴 선택
			int menu = Integer.parseInt(in.readLine());
			
			switch(menu) {
			//명함추가
			case 1:
				//sql 구문을 저장
				sql = "select * from card where name=?";
				
				//저장한 String 객체를 Connection의 PrepareStatement에 담아서
				//객체 ps에 저장한다
				ps = conn.prepareStatement(sql);
				
				System.out.print("이름 :");
				//setString -> 지정된 매개 변수를 지정된 문자열 값으로 설정한다
				//매개변수 -> (int index, java.lang.String tr)
				//PreparedStatement 인터페이스의 setString 메서드에 의해 지정된다.
				String addname = in.readLine();
				ps.setString(1, addname);
				
				//executeQuery -> 수행결과로 ResultSet객체의 값을 반환
				//SELECT 구문을 수행할 때 사용되는 함수
				rs = ps.executeQuery();
				
				if(!rs.next()) { //중복검사 
					// rs.next 입력값이 중복이 없으면 실행
					System.out.print("전화번호 :");
					String addtel = in.readLine();
					System.out.print("직책 :");
					String addposition = in.readLine();
					System.out.print("이메일 :");
					String addemail = in.readLine();
					
					sql = "insert into card values(?,?,?,?)";
					//prepareStatement 로 ?값에 String 값 대입
					ps = conn.prepareStatement(sql);
					ps.setString(1, addname);
					ps.setString(2, addtel);
					ps.setString(3, addposition);
					ps.setString(4, addemail);
					
					//업데이트가 1개 이상 되면 n값이 정수가 됨
					n = ps.executeUpdate();
					if(n > 0) {
						System.out.println("명함 정상 등록!");
					} else {
						System.out.println("명함 등록 실패!");
					}
				}else {
					System.out.println("동일 이름이 이미 존재합니다.");
				}
				break;
			//명함삭제
			case 2:
				System.out.print("DB에서 수정하실 이름 :");
				String delName = in.readLine();
				sql = "delete from card where name=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, delName);
				n = ps.executeUpdate();
				if(n>0) {
					System.out.println("명함 정상 삭제!");
				} else {
					System.out.println("명함 삭제 실패!");
				}
				break;
			//명함수정
			case 3:
				System.out.print("DB에서 수정하실 이름:");
				String modName = in.readLine();
				sql = "select * from card where name=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, modName);
				rs = ps.executeQuery();
				
				if(rs.next()) {
					System.out.print("이름:");
					String newName = in.readLine();
					System.out.print("전화번호:");
					String modTel = in.readLine();
					System.out.print("직책:");
					String modPosition = in.readLine();
					System.out.print("이메일:");
					String modEmail = in.readLine();
					
					sql = "update card set name=?,tel=?,position=?,email=? where name=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, newName);
					ps.setString(2, modTel);
					ps.setString(3, modPosition);
					ps.setString(4, modEmail);
					ps.setString(5, modName);
					
					ps.executeUpdate();
				}else {
					System.out.println("님은 등록되어 있지 않습니다.");
				}
				break;
			//명함보기
			case 4:
				sql = "select * from card";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				System.out.println("이 름\t전화번호\t직책\t이메일");
				while(rs.next()) {
					System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
				}
				break;
			case 5:
				System.out.println("프로그램 종료");
			}
		}
	}

}
