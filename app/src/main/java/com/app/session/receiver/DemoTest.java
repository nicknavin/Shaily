package com.app.session.receiver;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.pm.ComponentInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class DemoTest extends AppCompatActivity
{
    private static final int JOB_ID = 1001;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void callfun()
    {
        JobScheduler jobScheduler=(JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName=new ComponentName(this,MyJobScheduler.class);
        JobInfo jobInfo=new JobInfo.Builder(JOB_ID,componentName).setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build();
        jobScheduler.schedule(jobInfo);
    }
}
