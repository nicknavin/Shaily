package com.app.session.receiver;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobScheduler extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
jobFinished(jobParameters,true);
            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
