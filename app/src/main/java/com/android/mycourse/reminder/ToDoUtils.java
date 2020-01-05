package com.android.mycourse.reminder;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class ToDoUtils {
    /**
     * 返回数据库用户所有的任务
     *
     * @param context
     * @return
     * @throws Exception
     */
    static List<Todos> getAllTodos(Context context) {
        List<Todos> temp = new ArrayList<Todos>();
        List<Todos> findAll = new ToDoDao(context).getAllTask();
        Log.i("ToDoUtils","任务个数" + findAll.size());
        if (findAll != null && findAll.size() > 0) {
            temp.addAll(findAll);
        }
        return temp;
    }
}
