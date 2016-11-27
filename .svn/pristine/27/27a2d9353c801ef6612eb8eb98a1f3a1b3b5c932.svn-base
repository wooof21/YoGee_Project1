package tools;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SchoolPullParse {
	public static ArrayList<String> ParseXml(XmlPullParser parser,
			String keyword) {
		ArrayList<String> list = new ArrayList<String>();

		try {
			// 开始解析事件
			int eventType = parser.getEventType();

			// 处理事件，不碰到文档结束就一直处理
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// 因为定义了一堆静态常量，所以这里可以用switch
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					// 不做任何操作或初开始化数据
					break;

				case XmlPullParser.START_TAG:
					// 解析XML节点数据
					// 获取当前标签名字
					String tagName = parser.getName();

					if (tagName.equals("name")) {
						String school = parser.nextText();
						if (school.indexOf(keyword) != -1) {
							list.add(school);
						}
					}
					break;

				case XmlPullParser.END_TAG:
					// 单节点完成，可往集合里边添加新的数据
					break;
				case XmlPullParser.END_DOCUMENT:

					break;
				}

				// 别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
}
