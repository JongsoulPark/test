package test01;

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class Test {

	static String firstText = "안녕하세요. 점심 식사는 하셨나요?";
	// 실행 취소 시 불러오기 위한 스택 - edit 실행 시마다 쌓음
	static Stack<String> undo_stack = new Stack<String>();
	// 재실행 시 불러오기 위한 스택 - undo 실행 시마다 쌓음 
	static Stack<String> redo_stack = new Stack<String>();
	
	
	//프로그램 실행
	public static void main(String[] args) {
		
		// 초기값도 초기 출력이 되므로 실행 취소시 마지막에 출력되야한다. 따라서 실행취소 stack인 undo_stack에 저장
		undo_stack.push(firstText);
		
		System.out.println("=== 사용법은 list() 를 입력해주세요. ===");
		// 프로그램 실행 시 출력 문자열
		System.out.println(firstText);
		// input 값을 받기 위함 Scanner 클래서 생성
		Scanner scn = new Scanner(System.in);
		
	
		
		// 프로그램이 종료되지 않기 위해 while문 사용
		while(true) {
			
			String inputText = scn.nextLine();	
			
			
			// 입력받는 값이 처음 edit으로 시작는지 여부 확인
			if(inputText.startsWith("edit")) {
				
				// 입력받은 값에 괄호기호()가 포함되어 있는지 확인
				if(inputText.contains("(") && inputText.contains(")")) {
					System.out.println("정상 명령어 입니다.");
					String edit_condition = inputText.substring(inputText.indexOf("(") + 1 , inputText.lastIndexOf(")"));
					
					// 입력받은 값에 조건이 비어있는지 확인
					if(edit_condition != null && edit_condition != "") {
						System.out.println("조건이 비어있지 않음");
						String[] split_codition = edit_condition.split(",");
						
						// 입력받은 조건의 개수가 3개인지 확인
						if(split_codition.length == 3) {
							System.out.println("조건 개수 만족");
							
							
							// 입력 받은 1번째와 2번째 조건이 숫자인지 확인 + 앞뒤 공백 제거
							try {
								String first_condition = split_codition[0].trim();
								String second_condition = split_codition[1].trim();
								
								int x = Integer.parseInt(first_condition);
								int y = Integer.parseInt(second_condition);
								String change_text = "";
								
								// 입력 받은 3번째 조건에 ""가 존재하는지 확인 + 앞뒤 공백 제거
								if(split_codition[2].contains("\"")){
									String third_condition = split_codition[2].replaceAll("\"", "");
									change_text = third_condition.trim(); 
								
								}else {
									change_text = split_codition[2];
								
								}
								
								edit(x,y,change_text);
								
							}catch(NumberFormatException e){
								System.out.println("1번째 조건과 2번째 조건은 숫자로 입력해야 합니다.(특수문자는 사용하실 수 없음)");
								String peek_message = undo_stack.peek();
								System.out.println(peek_message);
							
							}
							
						}else {
							System.out.println("1번째, 2번째, 3번째 조건들 사이에 콤마(,)를 입력해주세요");
							String peek_message = undo_stack.peek();
							System.out.println(peek_message);
						
						}
					
					}else {
						System.out.println("조건이 비어있습니다. 조건을 입력해주세요.");
						String peek_message = undo_stack.peek();
						System.out.println(peek_message);
					
					}
				
				}else {
					System.out.println("잘못된 명령어 입니다.");
					String peek_message = undo_stack.peek();
					System.out.println(peek_message);
				
				}
			
				
				
			// 실행 취소 - 입력이 undo()와 같다면 실행
			}else if(inputText.equals("undo()")) {
				System.out.println("취소");
				undo();
			
			// 재실행 - 입력이 redo()와 같다면 실행
			}else if(inputText.equals("redo()")) {
				System.out.println("재실행");
				redo();
			
			// 종료
			}else if(inputText.equals("end")){
				System.out.println("종료");
				scn.close();
				break;
			
			}else if(inputText.equals("list()")) {
				System.out.println("====================사용법====================");
				System.out.println("문자열 번호 : 문자열 번호는 0부터 시작하며 특수문자와 띄워쓰기도 하나의 번호로 인식합니다.");
				System.out.println("예시 : 안녕하세요. 점심 식사는 하셨나요? 는 총 0~18 까지의 번호를 가지고 있습니다.");				
				System.out.println("편집 : edit( 바꿀 문자열 시작 번호, 바꿀 문자열 끝 번호 + 1, \"변경할 문자열 입력\")");
				System.out.println("실행 취소 : undo()  -  작업 전단계로 돌아갑니다.");
				System.out.println("재실행 : redo()  -  실행 취소 전단계로 돌아갑니다.");
				System.out.println("종료 : end  -  프로그램이 종료됩니다.");
				System.out.println("============================================");
				
				
			}else {
				System.out.println("잘못된 명령어 입니다. 다시 입력해 주세요");
				String peek_message = undo_stack.peek();
				System.out.println(peek_message);
				continue;
			}
				
		}
		
	}
		
	// 편집을 위한 메서드
	public static void edit(int x, int y, String change_text) {
		
		// 1번째 조건은 2번째 조건보다 작거나 같아야한다.
		if(x <= y) {
			
			StringBuffer sb = new StringBuffer();
			
			String edit_message = undo_stack.peek();
			sb.append(edit_message);
			
			// 1번째 조건의 값이 문자열 길이의 값을 초과하는지 확인
			try {
				sb.replace(x, y, change_text);
				
			}catch(StringIndexOutOfBoundsException e) {
				System.out.println("1번째 조건의 숫자는 문자열의 길이보다 클 수 없습니다. 다시 입력해주세요.");
			
			}
			
			String Message = sb.toString();
			undo_stack.push(Message);
			String peek_message = undo_stack.peek();
			System.out.println(peek_message);
			
		}else {
			System.out.println("1번째 조건 숫자는 2번째 조건 숫자보다 클 수 없습니다. 다시 입력해 주세요. 잘못된 예:edit(10,2,\"인사\") ");
			String peek_message = undo_stack.peek();
			System.out.println(peek_message);
			
		}
		
	}
	
	// 실행 취소를 위한 메서드
	public static void undo() {
		
		try {
			String undo_pop = undo_stack.pop();
			redo_stack.push(undo_pop);
			String peek_message = undo_stack.peek();
			System.out.println(peek_message);
		
		}catch(EmptyStackException e){
			System.out.println("더 이상 실행 취소를 할 수 없습니다. 시스템이 처음으로 돌아갑니다.");
			String peek_message = redo_stack.peek();
			System.out.println(peek_message);
		}
		
	}
	
	// 재실행 위한 메서드
	public static void redo() {
		
		try {
			String redo_pop = redo_stack.pop();
			undo_stack.push(redo_pop);
			String peek_message = undo_stack.peek();
			System.out.println(peek_message);
			
		}catch(EmptyStackException e){
			System.out.println("더 이상 재실행할 자료가 존재하지 않습니다. 편집(edit)을 실행해 주세요.");
			String peek_message = undo_stack.peek();
			System.out.println(peek_message);
		}
		
	}

}
