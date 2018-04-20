package com.example.noone.mvvmsampleone.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.noone.mvvmsampleone.data.source.TasksRepository;
import com.example.noone.mvvmsampleone.data.source.local.TasksLocalDataSource;
import com.example.noone.mvvmsampleone.data.source.local.ToDoDatabase;
import com.example.noone.mvvmsampleone.data.source.remote.TasksRemoteDataSource;
import com.example.noone.mvvmsampleone.utils.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        ToDoDatabase database = ToDoDatabase.getInstance(context);
        return TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(new AppExecutors(),
                        database.taskDao()));
    }
}
