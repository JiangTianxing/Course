package com.coursetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 爬完后统计了学号发现较全校学生人数少了好多
 * 后面发现是部分留学生还有一些特殊群体的要么没课要么通过普通接口根本拿不到
 */
public class Tasks {

    private static List<String> stuIds;

    private static List<Map<String, String>> courses;

    /**
     * 较第一版的做法
     * 这回省去了很多比较迷糊的地方
     * 另外发现原来的逻辑有点问题及时更正了一下
     * curl用URLConnection替换了HttpClient貌似爬去方面遇到的bug减少了
     */
    private Runnable item = ()->{
        //线程开始
        boolean status = true;
        while (status) {
            //当学号数组不为空时
            if (stuIds.size() != 0) {
                //当课表数量大于1000时，存入数据库
                if (courses.size() > 1000) {
                    insertCourse();
                }
                //取出一条学号
                //爬出该生课表
                //存入课表数组中
                getData();
                //当学号数组为空时
            } else {
                selectStuId();
                //如果学号数组还是为空，表名数据爬完
                if (stuIds.size() == 0) {
                    //把剩余课表插入数据库中
                    insertCourse();
                    //跳出循环
                    status = false;
                }
            }
        }
    };

    public static Runnable getRunnableInstance() {
        return new Tasks().item;
    }

    /**
     * 从学生表中取出学号，存入学号数组中
     */
    private static void selectStuId() {
        List<String> stuIdz = Action.selectStuId();
        stuIds.addAll(stuIdz);
    }

    /**
     * 插入课表数据表中
     */
    private static void insertCourse() {
        List<Map<String, String>> data = new ArrayList<>();
        synchronized (Tasks.class) {
            data.addAll(courses);
            courses.clear();
        }
        Action.insertCourseData(data);
    }

    /**
     * 取出一条学号
     * 爬出该生课表
     * 存入课表数组中
     */
    private static void getData() {
        if (!stuIds.isEmpty()) {
            String stuId = stuIds.remove(0);
            List<Map<String, String>> courez = Curl.getStuCourses(stuId);
            courses.addAll(courez);
        }
    }

    static {
        stuIds = new ArrayList<>();
        courses = new ArrayList<>();
    }
}
