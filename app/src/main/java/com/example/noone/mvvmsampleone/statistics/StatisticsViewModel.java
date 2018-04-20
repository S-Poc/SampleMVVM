package com.example.noone.mvvmsampleone.statistics;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.example.noone.mvvmsampleone.R;
import com.example.noone.mvvmsampleone.data.Task;
import com.example.noone.mvvmsampleone.data.source.TasksDataSource;
import com.example.noone.mvvmsampleone.data.source.TasksRepository;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);
    public final ObservableBoolean error = new ObservableBoolean(false);
    public final ObservableField<String> numberOfActiveTasks = new ObservableField<>();
    public final ObservableField<String> numberOfCompletedTasks = new ObservableField<>();

    /**
     * Controls whether the stats are shown or a "No data" message.
     */
    public final ObservableBoolean empty = new ObservableBoolean();


    private int mNumberOfActiveTasks = 0;
    private int mNumberOfCompletedTasks = 0;
    private final Context mContext;
    private final TasksRepository mTasksRepository;

    public StatisticsViewModel(Application context, TasksRepository tasksRepository) {
        super(context);
        mContext = context;
        mTasksRepository = tasksRepository;
    }

    public void start() {
        loadStatistics();
    }

    public void loadStatistics() {
        dataLoading.set(true);

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                error.set(false);
                computeStats(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                error.set(true);
                mNumberOfActiveTasks = 0;
                mNumberOfCompletedTasks = 0;
                updateDataBindingObservables();
            }
        });
    }

    private void computeStats(List<Task> tasks) {
        int completed = 0;
        int active = 0;

        for (Task task : tasks) {
            if (task.isCompleted()) {
                completed += 1;
            } else {
                active += 1;
            }
        }
        mNumberOfActiveTasks = active;
        mNumberOfCompletedTasks = completed;

        updateDataBindingObservables();
    }

    private void updateDataBindingObservables() {
        numberOfCompletedTasks.set(
                mContext.getString(R.string.statistics_completed_tasks, mNumberOfCompletedTasks));
        numberOfActiveTasks.set(
                mContext.getString(R.string.statistics_active_tasks, mNumberOfActiveTasks));
        empty.set(mNumberOfActiveTasks + mNumberOfCompletedTasks == 0);
        dataLoading.set(false);
    }
}
