package com.student.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.example.strudentlelts.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 日历gridview中的每一个item显示的textview
 * 
 * @author willm zhang
 *
 */
public class CalendarView extends BaseAdapter {
	private static final String TAG = "CalendarView";
	// private ScheduleDAO dao = null;
	private boolean isLeapyear = false; // 是否为闰年
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int lastDaysOfMonth = 0; // 上一个月的总天数

	private Context context;
	private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中

	private SpecialCalendar sc = null;

	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1; // 用于标记当天
	private int currentDateNum = -1; // 用于标记当天的时间;
	private int[] schDateTagFlag = null; // 存储当月所有的日程日期

	private String showYear = ""; // 用于在头部显示的年份
	private String showMonth = ""; // 用于在头部显示的月份
	// private String showDay = "";//用于在头部显示天
	private String animalsYear = "";
	private String leapMonth = ""; // 闰哪一个月
	private String cyclical = ""; // 天干地支
	// 系统当前时间
	private String sysDate = "";
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";

	// 日程时间(需要标记的日程日期)
	private String sch_year = "";
	private String sch_month = "";
	private String sch_day = "";

	// SpecialCalendar spe = new SpecialCalendar();

	// 设置 日期对应 课程树木的 数组
	private HashMap<String, Object> map_num;
	private List<String> l_s;

	//
	private boolean isfirst = true;
	//
	private int c_position;

	public CalendarView() {
		Date date = new Date();
		sysDate = sdf.format(date); // 当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
	}

	public CalendarView(Context context, int jumpMonth, int jumpYear,
			int year_c, int month_c, int day_c, HashMap<String, Object> map_test) {
		this();
		this.context = context;
		this.map_num = map_test;
		sc = new SpecialCalendar();

		int stepYear = year_c + jumpYear;
		int stepMonth = month_c + jumpMonth;
		if (stepMonth > 0) {
			// 往下一个月跳转
			if (stepMonth % 12 == 0) {
				stepYear = year_c + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = year_c + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else {
			// 往上一个月跳转
			stepYear = year_c - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
			if (stepMonth % 12 == 0) {

			}
		}

		currentYear = String.valueOf(stepYear); // 得到当前的年份
		currentMonth = String.valueOf(stepMonth); // 得到本月
													// （jumpMonth为跳动的次数，每滑动一次就增加一月或减一月）
		currentDay = String.valueOf(day_c); // 得到当前日期是哪天

		System.out.println("当前的日期为=======" + currentYear + "--" + currentMonth
				+ "--" + currentDay);

		getCalendar(Integer.parseInt(currentYear),
				Integer.parseInt(currentMonth));

	}

	@Override
	public int getCount() {
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoler holder = new ViewHoler();

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.calendarview, null);
			holder.tView = (TextView) convertView
					.findViewById(R.id.textview_day);
			holder.main = (RelativeLayout) convertView.findViewById(R.id.main);

			// holder.icon = (ImageView) convertView.findViewById(R.id.icon);

			holder.dot = (ImageView) convertView.findViewById(R.id.dot);
			holder.dot1 = (ImageView) convertView.findViewById(R.id.dot1);
			holder.dot2 = (ImageView) convertView.findViewById(R.id.dot2);
			holder.dot3 = (ImageView) convertView.findViewById(R.id.dot3);
			holder.dot4 = (ImageView) convertView.findViewById(R.id.dot4);
			holder.dot5 = (ImageView) convertView.findViewById(R.id.dot5);

			holder.relative_day = (RelativeLayout) convertView
					.findViewById(R.id.relative_day);
			holder.iv_day = (ImageView) convertView
					.findViewById(R.id.iv_day);

			convertView.setTag(holder);
		} else {
			holder = (ViewHoler) convertView.getTag();
			resetViewHolder(holder);
		}

		// ----------------------------------------------------------------
		// 设置灰色的默认选项
		// String position_f = String.format("%02d", position);
		//
		// List<String> l_s = new ArrayList<String>();
		//
		// Set set = map_num.keySet();
		// Iterator iter = set.iterator();
		// while (iter.hasNext()) {
		// String key = (String) iter.next();
		// l_s.add(key);
		// }
		//
		// for (int i = 0; i < l_s.size(); i++) {
		// String str = l_s.get(i);
		// if (str.equalsIgnoreCase(String.valueOf(position_f))) {
		//
		// System.out.println("里面含有课程  字体背景变色 ");
		// holder.relative_day.setBackground(context.getResources()
		// .getDrawable(R.drawable.calendar_background_grey));
		//
		// }
		// }
		// ---------------------------------------------------------------------

		String d = dayNumber[position].split("\\.")[0];
		SpannableString sp = new SpannableString(d);

		holder.tView.setText(sp);
		holder.tView.setTextColor(context.getResources().getColor(
				R.color.describe_color));

		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			// 当前月信息显示
			holder.tView.setTextColor(context.getResources().getColor(
					R.color.black));
		}

		if (currentFlag == position) {

			if (isfirst) {
				c_position = position;
				// holder.tView.setBackgroundResource(R.drawable.calendar_background_red);
				holder.tView.setTextColor(Color.WHITE);
				holder.iv_day
						.setBackgroundResource(R.drawable.calendar_background_red);
				isfirst = false;
			} else {

				if (c_position == position) {
					// 设置当天的背景
					holder.tView.setTextColor(Color.WHITE);
					holder.iv_day
							.setBackgroundResource(R.drawable.calendar_background_red);
				} else {
					// 设置当天的背景
					holder.tView.setTextColor(Color.WHITE);
					holder.iv_day
							.setBackgroundResource(R.drawable.calendar_background_red);
					
				}

				// 设置当天的背景
				// holder.tView.setTextColor(Color.WHITE);
				// holder.relative_day.setBackgroundResource(R.drawable.calendar_background_red);
			}
		} else {
			holder.tView.setTextColor(context.getResources().getColor(
					R.color.black));
			// holder.dot.setVisibility(View.INVISIBLE);
			// holder.main.setBackgroundColor(context.getResources().getColor(R.color.calendar_bg2));
			//holder.relative_day.setBackgroundColor(Color.WHITE);
			holder.iv_day.setBackgroundResource(R.color.transparent);
			/*if(nowFlag == position){
				holder.tView.setTextColor(Color.RED);
			}*/
		}
		/*if (position < dayOfWeek || position >= daysOfMonth + dayOfWeek) {
			// 设置上一月和下一月的背景
			holder.tView.setTextColor(context.getResources().getColor(
					R.color.describe_color));
		}*/
		
		
		
		
		

		if (schDateTagFlag != null && schDateTagFlag.length > 0) {
			for (int i = 0; i < schDateTagFlag.length; i++) {
				Log.e(TAG,"schDateTagFlag-----------------"+schDateTagFlag[i]);
				if (schDateTagFlag[i] == position) {

					if (currentFlag == position) {
						// 设置日程标记背景
						holder.iv_day
								.setBackgroundResource(R.drawable.calendar_background_red);

						holder.tView.setTextColor(context.getResources()
								.getColor(R.color.white));
					} else {
						// 设置日程标记背景
						holder.iv_day
								.setBackgroundResource(R.drawable.morenhui);

						holder.tView.setTextColor(context.getResources()
								.getColor(R.color.white));

						if (currentDateNum == position) {
							holder.tView.setTextColor(Color.RED);
							//holder.tView.setBackgroundColor(Color.WHITE);
							if (currentFlag != position) {
								holder.tView.setTextColor(Color.RED);
								holder.iv_day.setBackgroundResource(R.color.white);
							} else {
								holder.tView.setTextColor(Color.WHITE);
							}
						} else {
							holder.tView.setTextColor(Color.WHITE);
						}

					}

					// System.out.println("=============schDateTagFlag" + "[" +
					// i+ "]=="+ position );
					// 设置日程标记背景
					// holder.tView
					// .setBackgroundResource(R.drawable.calendar_background_grey);
					// System.out.println("=============schDateTagFlag" + "[" +
					// i
					// + "]==" + schDateTagFlag[i] + "====+" + position);

					String dd = String.format("%02d",
							Integer.valueOf(holder.tView.getText().toString()));
					String mm = String.format("%02d",
							Integer.valueOf(currentMonth));
					//
					String test = currentYear + "-" + mm + "-" + dd;

					System.out.println("拼接的数据位=日期 =========" + test);
					//
					String s ="0";
					if (!map_num.containsKey(test)) {

					} else {
						s= map_num.get(test).toString();
						System.out.println(position + "中有多少个课程==" + s);

						String ss = map_num.get(l_s.get(i)).toString();
					}
					int isvisi = Integer.valueOf(s);
					System.out.println("将要显示几个dot（点）" + isvisi);

					switch (isvisi) {
					case 0:
						holder.dot.setVisibility(View.GONE);
						holder.dot1.setVisibility(View.GONE);
						holder.dot2.setVisibility(View.GONE);
						holder.dot3.setVisibility(View.GONE);
						holder.dot4.setVisibility(View.GONE);
						holder.dot5.setVisibility(View.GONE);
						break;
					case 1:
						holder.dot1.setVisibility(View.VISIBLE);
						break;
					case 2:
						holder.dot.setVisibility(View.VISIBLE);
						holder.dot1.setVisibility(View.VISIBLE);
						break;
					case 3:
						holder.dot.setVisibility(View.VISIBLE);
						holder.dot1.setVisibility(View.VISIBLE);
						holder.dot2.setVisibility(View.VISIBLE);
						break;
					case 4:
						holder.dot.setVisibility(View.VISIBLE);
						holder.dot1.setVisibility(View.VISIBLE);
						holder.dot2.setVisibility(View.VISIBLE);
						holder.dot3.setVisibility(View.VISIBLE);
						break;
					case 5:
						holder.dot.setVisibility(View.VISIBLE);
						holder.dot1.setVisibility(View.VISIBLE);
						holder.dot2.setVisibility(View.VISIBLE);
						holder.dot3.setVisibility(View.VISIBLE);
						holder.dot4.setVisibility(View.VISIBLE);
						break;
					case 6:
						holder.dot.setVisibility(View.VISIBLE);
						holder.dot1.setVisibility(View.VISIBLE);
						holder.dot2.setVisibility(View.VISIBLE);
						holder.dot3.setVisibility(View.VISIBLE);
						holder.dot4.setVisibility(View.VISIBLE);
						holder.dot5.setVisibility(View.VISIBLE);
						break;
					/*case 7:
					case 8:
					case 9:
					case 10:
					case 11:
						holder.dot.setVisibility(View.VISIBLE);
						holder.dot1.setVisibility(View.VISIBLE);
						holder.dot2.setVisibility(View.VISIBLE);
						holder.dot3.setVisibility(View.VISIBLE);
						holder.dot4.setVisibility(View.VISIBLE);
						holder.dot5.setVisibility(View.VISIBLE);
						break;*/
					default:
						holder.dot.setVisibility(View.VISIBLE);
						holder.dot1.setVisibility(View.VISIBLE);
						holder.dot2.setVisibility(View.VISIBLE);
						holder.dot3.setVisibility(View.VISIBLE);
						holder.dot4.setVisibility(View.VISIBLE);
						holder.dot5.setVisibility(View.VISIBLE);
						break;
					}
					break;

				}
				else {
					if (currentDateNum == position) {
						if (currentFlag != position) {
							holder.tView.setTextColor(Color.RED);
							holder.iv_day.setBackgroundResource(R.color.white);
						} else {
							holder.tView.setTextColor(Color.WHITE);
						}
					} else {
						if (currentFlag == position) {
							holder.tView.setTextColor(Color.WHITE);
						} else {
							holder.tView.setTextColor(Color.BLACK);
						}
					}
				}
			}
		}
		
		if (position < dayOfWeek || position >= daysOfMonth + dayOfWeek) {
			// 设置上一月和下一月的背景
			holder.tView.setTextColor(context.getResources().getColor(
					R.color.rili));
		}

		return convertView;
	}

	// 得到某年的某月的天数且这月的第一天是星期几
	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // 是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
		Log.d("DAY", isLeapyear + " ======  " + daysOfMonth
				+ "  ============  " + dayOfWeek + "  =========   "
				+ lastDaysOfMonth);
		getweek(year, month);
	}

	private ArrayList<ScheduleDateTag> getTagDate(int year, int month) {
		ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>() {
			private static final long serialVersionUID = -5976649074350323408L;
		};
		// int i = 1;
		// while (i < 10) {
		// int tagID = i;
		// int year1 = 2015;
		// int month1 = 7;
		// int day = 2 * (i);
		// int scheduleID = i;
		// ScheduleDateTag dateTag = new ScheduleDateTag(tagID, year1, month1,
		// day, scheduleID);
		// dateTagList.add(dateTag);
		// i++;
		// }
		// map_num 数组 <日期，课程数量>
		l_s = new ArrayList<String>();
		System.out.println("map_num==size==" + map_num.size());
//		@SuppressWarnings("rawtypes")
		Set set = map_num.keySet();
//		@SuppressWarnings("rawtypes")
		Iterator iter = set.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			l_s.add(key);
		}
		// 07-16 17:09:25.466: I/System.out(28395): l_s的key数据为=====2015-07-05
		for (int j = 0; j < l_s.size(); j++) {
			System.out.println("l_s的key数据为=====" + l_s.get(j));
			String[] result = l_s.get(j).split("-");
			
			String str_day = result[2];
			String str_month = result[1];
			String str_year = result[0];

			 System.out.println("截取之后的数据为==" + str_year + "__" + str_month
			 + "__" + str_day);

			int tagID = Integer.valueOf(str_day);
			int year1 = Integer.valueOf(str_year);
			int month1 = Integer.valueOf(str_month);
			int day = Integer.valueOf(str_day);
			int scheduleID = Integer.valueOf(str_day);

			ScheduleDateTag dateTag = new ScheduleDateTag(tagID, year1, month1,
					day, scheduleID);

			dateTagList.add(dateTag);
		}

		if (dateTagList != null && dateTagList.size() > 0) {
			return dateTagList;
		}
		return null;
	}

	// 将一个月中的每一天的值添加入数组dayNuber中
	private void getweek(int year, int month) {
		int j = 1;
		int flag = 0;

		// 得到当前月的所有日程日期(这些日期需要标记)
		// dao = new ScheduleDAO(context);
		ArrayList<ScheduleDateTag> dateTagList = this.getTagDate(year, month);
		if (dateTagList != null && dateTagList.size() > 0) {
			schDateTagFlag = new int[dateTagList.size()];
			
		
			
//			for (int m = 0; m < dateTagList.size(); m++) {
//				ScheduleDateTag dateTag = dateTagList.get(m);
//				int matchYear = dateTag.getYear();
//				int matchMonth = dateTag.getMonth();
//				int matchDay = dateTag.getDay();
//				if (matchYear == year && matchMonth == month
//						&& matchDay == Integer.parseInt(day)) {
//					System.out.println("matchYear===" + matchYear);
//					System.out.println("matchMonth===" + matchMonth);
//					System.out.println("matchDay===" + matchDay);
//					System.out.println("schDateTagFlag==[i]=" + i);
//					schDateTagFlag[flag] = i;
//					flag++;
//				}
//			}
			
		}
		System.out.println("dayNumber=============" + dayNumber.length);
		for (int i = 0; i < dayNumber.length; i++) {
			// 锟斤拷一
			if (i < dayOfWeek) { // //前一个月
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				dayNumber[i] = (temp + i) + ".";

			} else if (i < daysOfMonth + dayOfWeek) { // 本月
				String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
				dayNumber[i] = i - dayOfWeek + 1 + ".";
				// 对于当前月才去标记当前日期
				if (sys_year.equals(String.valueOf(year))
						&& sys_month.equals(String.valueOf(month))) {
					// hsl add
					if (sys_day.equals(day)) {
						// 笔记当前日期
						currentFlag = i;
						//nowFlag=i;
						currentDateNum = i;
					}
				} else {
					// hsl add
					currentFlag = dayOfWeek;
					//nowFlag=dayOfWeek;
				}
				// 标记日程日期
				System.out.println("标记日程日期=======dateTagList======" + dateTagList);
				if (dateTagList != null && dateTagList.size() > 0) {
					for (int m = 0; m < dateTagList.size(); m++) {
						ScheduleDateTag dateTag = dateTagList.get(m);
						int matchYear = dateTag.getYear();
						int matchMonth = dateTag.getMonth();
						int matchDay = dateTag.getDay();
						if (matchYear == year && matchMonth == month
								&& matchDay == Integer.parseInt(day)) {
							System.out.println("matchYear===" + matchYear);
							System.out.println("matchMonth===" + matchMonth);
							System.out.println("matchDay===" + matchDay);
							System.out.println("schDateTagFlag==[i]=" + i);
							schDateTagFlag[flag] = i;
							flag++;
						}
					}

				}
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
			} else { // 下一个月
				dayNumber[i] = j + ".";
				j++;
			}
		}

	}

	class ViewHoler {
		RelativeLayout main;
		TextView tView;
		// ImageView icon;
		ImageView dot;
		ImageView dot1;
		ImageView dot2;
		ImageView dot3;
		ImageView dot4;
		ImageView dot5;
		RelativeLayout relative_day;
		ImageView iv_day;
	}

	private void resetViewHolder(ViewHoler vh) {
		// vh.icon.setImageDrawable(null);
		// vh.icon.setImageDrawable(null);
	}

	public int getCurrentFlag() {
		return currentFlag;
	}

	public void setCurrentFlag(int currentFlag) {
		this.currentFlag = currentFlag;
	}

	public void matchScheduleDate(int year, int month, int day) {

	}

	/**
	 * 点击每一个item时返回item中的日期
	 * 
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position) {
		return dayNumber[position];
	}

	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * 
	 * @return
	 */
	public int getStartPositon() {
		return dayOfWeek;
	}

	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * 
	 * @return
	 */
	public int getEndPosition() {
		return (dayOfWeek + daysOfMonth) - 1;
	}

	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}

	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}

	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}

	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}

}