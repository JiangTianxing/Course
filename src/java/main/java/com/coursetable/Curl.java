package com.coursetable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Curl {

    private static List<String> field;

    /**
     * 字段
     */
    static {
        field = new ArrayList<>();
        field.add("type");
        field.add("courseId");
        field.add("name");
        field.add("teacher");
        field.add("location");
        field.add("time");
        field.add("score");
        field.add("status");
    }

    /**
     * 获取学生课程信息
     * @param stuId
     * @return
     */
    public static List<Map<String, String>> getStuCourses(String stuId) {
        List<Map<String, String>> result = null;
        String data = curlForData(stuId);
        if (data != null && !data.equals("")) {
            result = new ArrayList<>();
            Pattern pTr = Pattern.compile("<tr>(.*?)</tr>");
            Matcher mTr = pTr.matcher(data);
            int i = 0;
            while (mTr.find()) {
                if ((i++) > 0) {
                    String line = mTr.group(1);
                    Map<String, String> item = new HashMap<>();
                    Pattern pTd = Pattern.compile("<td>(.*?)</td>");
                    Matcher mTd = pTd.matcher(line);
                    int j = 0;
                    while (mTd.find()) {
                        String key = field.get(j++);
                        String value = mTd.group(1);
                        item.put(key, value);
                    }
                    item.put("stuId", stuId);
                    result.add(item);
                }
            }
        }
        System.out.println(result.toString());
        return result;
    }

    /**
     * 爬去信息
     * @param stuId
     * @return
     */
    private static String curlForData(String stuId) {
        String result = null;
        try {
            String strUrl = "http://jwc.cqupt.edu.cn/showUserKebiao.php";
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            connection.getOutputStream(), "UTF-8"
                    )
            );
            StringBuilder temp = new StringBuilder();
            temp.append("id=")
                    .append(stuId)
                    .append("&type=stu");
            writer.write(temp.toString());
            writer.flush();
            writer.close();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream(), "UTF-8"
                    )
            );
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            result = builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
