package com.lelts.tool;

import java.util.ArrayList;
import java.util.List;

public class Guid {

	public static void main(String[] args) {
		// 8 4 4 4 12
		// 44CB8C3B-1CBB-3BDA-1B03-F3E18D42C59D
		// 6C024CC1-99E3-F88B-2C57-F0907A7A1801
		// E337CD73-4814-15A6-AB20-9C73ABDE7875
		// E823A410-9B83-E12E-01DA-50C99CFFB96B
		// 1B8A5253-BC6D-C977-F9CD-31A01A1FCF9E
		char A_Z[] = { 'A', 'B', 'C', 'D', 'E', 'F' };
		List<String> guidList = new ArrayList<String>();
		for (int j = 0; j < 50000; j++) {
			String guid = "";
			String syw = "";// 上一位
			for (int i = 0; i < 36; i++) {
				if (i == 8 || i == 13 || i == 18 || i == 23) {
					guid = guid + "-";
				} else {
					int tt = (int) (Math.random() * 1000);
					int tt1 = (int) (Math.random() * 100);
					int num = tt - tt1;
					if (num < 0 && !syw.equals("0")) {
						guid = guid + "0";
						syw = "0";
					} else {
						String str = num + "";
						int lastNum = Integer.parseInt((str.substring(str
								.length() - 1)));
						if (lastNum < A_Z.length) {
							// 为避免在新生成的guid中，数字只有数字6以上的，下面做了处理
							if ((tt - i) % 2 == 0 && !syw.equals(lastNum + "")) {
								guid = guid + lastNum;
								syw = lastNum + "";
							} else {
								if (!syw.equalsIgnoreCase(A_Z[lastNum] + "")) {
									guid = guid + A_Z[lastNum];
									syw = A_Z[lastNum] + "";
								} else {
									if (lastNum == 0) {
										guid = guid + A_Z[lastNum + 1];
										syw = A_Z[lastNum + 1] + "";
									} else {
										guid = guid + A_Z[lastNum - 1];
										syw = A_Z[lastNum - 1] + "";
									}
								}
							}
						} else {
							// 为避免6,7,8,9 出现多次，下面做了处理
							if ((tt - lastNum) % 2 == 0
									&& !syw.equals((10 - lastNum) + "")) {
								guid = guid + (10 - lastNum);
								syw = (10 - lastNum) + "";
							} else {
								if (!syw.equals(lastNum + "")) {
									guid = guid + lastNum;
									syw = lastNum + "";
								} else {
									guid = guid + (lastNum - 1);
									syw = (lastNum - 1) + "";
								}
							}
						}
					}
				}
			}
			guidList.add(guid);
		}

		System.out.println(guidList.size());
		for (int g = 0; g < guidList.size(); g++) {
			String guid = guidList.get(g).trim(); // 0
			// 判断当前的 guid中相邻的两个字符是否相同
			// char guidch[]=guid.toCharArray();
//			for (int o = 0; o < guidch.length - 2; o++) {
				// if( guidch[o]==guidch [o+1]){
				// System.out.println( guid);
				// break;
				// }
				// }
				// 判断当前X万条 guid中是否有重复的
				for (int u = 0; u < guidList.size(); u++) {
					if (g != u) {
						String guids = guidList.get(u).trim();
						if (guid.equals(guids)) {
							System.out.println(guid + "--" + g + "====="
									+ guids + "---" + u);

						}

					}
				}
			}
		}
	}
