package com.app.session.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ResultReceiver;

import android.util.Log;

import com.app.session.R;

import com.app.session.activity.ConsultantStoryActivity;
import com.app.session.model.ReqSendStory;
import com.app.session.notification.NotificationHelper;
import com.app.session.receiver.FileProgressReceiver;
import com.app.session.receiver.RetryJobReceiver;
import com.app.session.utils.MIMEType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.app.session.receiver.RetryJobReceiver.ACTION_CLEAR;
import static com.app.session.receiver.RetryJobReceiver.ACTION_RETRY;

public class FileUploadService extends JobIntentService {
    private static final String TAG = "FileUploadService";
    RestApiService apiService;

    Disposable mDisposable;
    public static final int NOTIFICATION_ID = 1;
    public static final int NOTIFICATION_RETRY_ID = 2;
    ResponseBody responseBody;
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 102;
    String mFilePath, mFileName, languageId = "", userId = "", token, mFilePathThumb = "";
    Bitmap videoThumbBitmap;
    //story data
    String storyType = "", storyTitle = "", storyText = "", videoUrl = "", subscription_id = "";
    String requestType = "";

    NotificationHelper mNotificationHelper;

    private ResultReceiver mResultReceiver;
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    public static final String RECEIVER = "receiver";
    public static final int SHOW_RESULT = 123;
    public static final int STATUS = 123456;
    public static final int FAIL = 1234;
    static final int DOWNLOAD_JOB_ID = 1000;
    ReqSendStory sendStory;
    /*...................Story parameter.........................*/
    RequestBody reqStoryText, reqStoryTitle, reqStoryType, reqSubscriptionId, reqVideoUrl, reqDocFileName;
    RequestBody requestfile = null;
    MultipartBody.Part productimg = null;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationHelper = new NotificationHelper(this);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork: ");
        /**
         * Download/Upload of file
         * The system or framework is already holding a wake lock for us at this point
         */
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_DOWNLOAD:

                    mResultReceiver = intent.getParcelableExtra("REC");
//                    for(int i=0;i<10;i++){
//                        try {
//                            Thread.sleep(1000);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("data",String.format("Showing From JobIntent Service %d", i));
//                            mResultReceiver.send(SHOW_RESULT, bundle);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    break;
            }
        }


        // get file file here
        mFilePath = intent.getStringExtra("mFilePath");
        mFileName = intent.getStringExtra("FileName");
        languageId = intent.getStringExtra("LANGUAGE_ID");
        userId = intent.getStringExtra("USER_ID");
        token = intent.getStringExtra("TOKEN");
        if (intent.getStringExtra("TYPE") != null) {
            requestType = intent.getStringExtra("TYPE");
            if (requestType.equals("story"))
            {
                if (intent.getParcelableExtra("DATA") != null) {
                    sendStory = intent.getParcelableExtra("DATA");
                }
                if ((Bitmap) intent.getParcelableExtra("VIDEO_THUMB") != null) {

                    videoThumbBitmap = (Bitmap) intent.getParcelableExtra("VIDEO_THUMB");

                }

            }
        }
        RequestBody usercd = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody language_Id = RequestBody.create(MediaType.parse("text/plain"), languageId);

        if (requestType.equals("story"))
        {
            imageFile = persistImage(videoThumbBitmap, "story_image");

            reqStoryText = RequestBody.create(MediaType.parse("text/plain"), sendStory.getStoryText());
            reqStoryTitle = RequestBody.create(MediaType.parse("text/plain"), sendStory.getStoryTitle());
            reqStoryType = RequestBody.create(MediaType.parse("text/plain"), sendStory.getStoryType());
            reqVideoUrl = RequestBody.create(MediaType.parse("text/plain"), sendStory.getVideoUrl());
            reqSubscriptionId = RequestBody.create(MediaType.parse("text/plain"), sendStory.getSubscription_id());
            reqDocFileName = RequestBody.create(MediaType.parse("text/plain"), sendStory.getDocFileName());

            if (imageFile != null) {
                requestfile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
                if (sendStory.getStoryType().equals("image"))
                {
                    productimg = MultipartBody.Part.createFormData("file", "image.jpg", requestfile);

                } else if (sendStory.getStoryType().equals("video")) {
                    productimg = MultipartBody.Part.createFormData("thumbnail", "image.jpg", requestfile);
                }

            }
        }


        if (mFilePath == null) {
            Log.e(TAG, "onHandleWork: Invalid file URI");
            return;
        }
        if (requestType.equals("story")) {

            apiService = RetrofitInstance.getApiStoryService();
        } else {
            apiService = RetrofitInstance.getApiService();
        }


        Flowable<Double> fileObservable = Flowable.create(new FlowableOnSubscribe<Double>() {
            @Override
            public void subscribe(FlowableEmitter<Double> emitter) throws Exception {
                if (requestType.equals("story")) {

                    if (sendStory.getStoryType().equals("video")) {
                        if (sendStory.getSubscription_id().isEmpty()) {

                            responseBody = apiService.reqUserSendsStory(token, usercd, reqStoryType, reqStoryTitle, reqStoryText, FileUploadService.this.createStoryMultipartBody("file", mFilePath, emitter), productimg).blockingGet();

                        } else {

                            responseBody = apiService.reqSendsStory(token, usercd, reqSubscriptionId, reqStoryType, reqStoryTitle, reqStoryText, FileUploadService.this.createStoryMultipartBody("file", mFilePath, emitter), productimg).blockingGet();
                        }
                    } else if (sendStory.getStoryType().equals("image")) {
                        if (sendStory.getSubscription_id().isEmpty()) {

                            responseBody = apiService.reqUserSendsStoryImage(token, usercd, reqStoryType, reqStoryTitle, reqStoryText, reqVideoUrl, FileUploadService.this.createMultipartBodyImage("file", "image.jpg", "image/jpeg", imageFile, emitter)).blockingGet();

                        } else {

                            responseBody = apiService.reqUserSendsStoryImage1(token, usercd, reqSubscriptionId, reqStoryType, reqStoryTitle, reqStoryText, reqVideoUrl, FileUploadService.this.createMultipartBodyImage("file", "image.jpg", "image/jpeg", imageFile, emitter)).blockingGet();
                        }
                    } else if (sendStory.getStoryType().equals("anydoc")) {
                        File file = new File(sendStory.getDocFilePath());
                        if (sendStory.getSubscription_id().isEmpty()) {

                            responseBody = apiService.callUserSendStoryDocFile(token, usercd, reqStoryType, reqStoryTitle, reqStoryText, reqDocFileName, FileUploadService.this.createMultipartBodyImage("file", file.getName(), "multipart/form-data",file, emitter)).blockingGet();

                        } else {
                            responseBody = apiService.callSendStoryDocFile(token, usercd, reqSubscriptionId, reqStoryType, reqStoryTitle, reqStoryText, reqDocFileName, FileUploadService.this.createMultipartBodyImage("file", file.getName(), "multipart/form-data", file, emitter)).blockingGet();
                        }
                    }
                    else if (sendStory.getStoryType().equals("audio"))
                    {
                        File file = new File(sendStory.getDocFilePath());


                        if (sendStory.getSubscription_id().isEmpty()) {

                            responseBody = apiService.callUserSendAudioStory1(token,usercd,reqStoryType,reqStoryTitle,reqStoryText,reqVideoUrl,FileUploadService.this.createMultipartBodyImage("file", file.getName(), "audio/*", file, emitter)).blockingGet();

                        } else {
                            responseBody = apiService.callSendAudioStory1(token,usercd,reqSubscriptionId,reqStoryType,reqStoryTitle,reqStoryText,reqVideoUrl,FileUploadService.this.createMultipartBodyImage("file", file.getName(), "audio/*", file, emitter)).blockingGet();
                        }


                    }


                } else {
                    responseBody = apiService.onFileUpload(token, usercd, language_Id, FileUploadService.this.createMultipartBody(mFilePath, emitter)).blockingGet();


                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);


        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Double>() {
                    @Override
                    public void accept(Double progress) throws Exception {
                        // call onProgress()
                        FileUploadService.this.onProgress(progress);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // call onErrors() if error occurred during file upload

                        FileUploadService.this.onErrors(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        // call onSuccess() while file upload successful
                        FileUploadService.this.onSuccess();
                    }
                });
    }


    private void calldfdsf() {
//        try {
//            String data= responseBody.string();
//            System.out.println("video upload result  "+data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    File imageFile;

    private File persistImage(Bitmap bitmap, String name) {

        File filesDir = this.getFilesDir();
        imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        return imageFile;

    }


    private void onErrors(Throwable throwable) {
        /**
         * Error occurred in file uploading
         */

        try {

            Bundle bundle = new Bundle();

            if (responseBody != null) {

                String dfds = responseBody.toString();
                String data = responseBody.string();
                System.out.println("video upload result  " + data);
                bundle.putString("STATUS", "Done");
                bundle.putString("DATA", data);
                mResultReceiver.send(FAIL, bundle);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent successIntent = new Intent("com.wave.ACTION_CLEAR_NOTIFICATION");
        successIntent.putExtra("notificationId", NOTIFICATION_ID);
        sendBroadcast(successIntent);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, new Intent(this, ConsultantStoryActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         * Add retry action button in notification
         */
        Intent retryIntent = new Intent(this, RetryJobReceiver.class);
        retryIntent.putExtra("notificationId", NOTIFICATION_RETRY_ID);
        retryIntent.putExtra("mFilePath", mFilePath);
        retryIntent.setAction(ACTION_RETRY);

        /**
         * Add clear action button in notification
         */
        Intent clearIntent = new Intent(this, RetryJobReceiver.class);
        clearIntent.putExtra("notificationId", NOTIFICATION_RETRY_ID);
        clearIntent.putExtra("mFilePath", mFilePath);
        clearIntent.setAction(ACTION_CLEAR);

        PendingIntent retryPendingIntent = PendingIntent.getBroadcast(this, 0, retryIntent, 0);
        PendingIntent clearPendingIntent = PendingIntent.getBroadcast(this, 0, clearIntent, 0);
        NotificationCompat.Builder mBuilder = mNotificationHelper.getNotification(getString(R.string.error_upload_failed), getString(R.string.message_upload_failed), resultPendingIntent);
        // attached Retry action in notification
        mBuilder.addAction(android.R.drawable.ic_menu_revert, getString(R.string.btn_retry_not), retryPendingIntent);
        // attached Cancel action in notification
        mBuilder.addAction(android.R.drawable.ic_menu_revert, getString(R.string.btn_cancel_not), clearPendingIntent);
        // Notify notification
        mNotificationHelper.notify(NOTIFICATION_RETRY_ID, mBuilder);





    }

    /**
     * Send Broadcast to FileProgressReceiver with progress
     *
     * @param progress file uploading progress
     */
    boolean flag=false;
    private void onProgress(Double progress) {


        Bundle bundle = new Bundle();
        bundle.putInt("progress", (int) (100 * progress));
        if(!flag) {
            bundle.putParcelable("IMAGE", videoThumbBitmap);
            bundle.putString("TYPE", sendStory.getStoryType());
            bundle.putString("FILENAME", sendStory.getDocFileName());
            System.out.println("filename"+sendStory.getDocFileName());
            flag=true;
        }
        mResultReceiver.send(SHOW_RESULT, bundle);

        Intent progressIntent = new Intent(this, FileProgressReceiver.class);
        progressIntent.setAction("com.wave.ACTION_PROGRESS_NOTIFICATION");
        progressIntent.putExtra("notificationId", NOTIFICATION_ID);
        progressIntent.putExtra("progress", (int) (100 * progress));
        sendBroadcast(progressIntent);
    }

    /**
     * Send Broadcast to FileProgressReceiver while file upload successful
     */
    private void onSuccess() {
        String data="";

        try {

//            Bundle bundle = new Bundle();
            data = responseBody.string();
//            bundle.putString("STATUS", "Done");
//            bundle.putString("DATA", data);
//            mResultReceiver.send(STATUS, bundle);
//
//            System.out.println("video upload result  " + data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent successIntent = new Intent(this, FileProgressReceiver.class);
        successIntent.setAction("com.wave.ACTION_UPLOADED");
        successIntent.putExtra("notificationId", NOTIFICATION_ID);
        successIntent.putExtra("progress", 100);
        successIntent.putExtra("DATA",data);
        successIntent.putExtra("RECEVIER",mResultReceiver);
        sendBroadcast(successIntent);

    }

    private RequestBody createRequestBodyFromFile(File file, String mimeType) {
        return RequestBody.create(MediaType.parse(mimeType), file);
    }

    private RequestBody createRequestBodyFromText(String mText) {
        return RequestBody.create(MediaType.parse("text/plain"), mText);
    }


    /**
     * return multi part body in format of FlowableEmitter
     *
     * @param filePath
     * @param emitter
     * @return
     */
    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);

        return MultipartBody.Part.createFormData("video", mFileName, createCountingRequestBody(file, MIMEType.VIDEO.value, emitter));
    }

    private MultipartBody.Part createMultipartBodyImage(String key, String fileName, String mimetype, File file, FlowableEmitter<Double> emitter) {

        return MultipartBody.Part.createFormData(key, fileName, createCountingRequestBody(file, mimetype, emitter));
    }


    private MultipartBody.Part createStoryMultipartBody(String name, String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);

        return MultipartBody.Part.createFormData(name, file.getName(), createCountingRequestBody(file, MIMEType.VIDEO.value, emitter));
    }

    private MultipartBody.Part createStoryImageMultipartBody(String name, String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);

        return MultipartBody.Part.createFormData(name, file.getName(), createCountingRequestBody(file, MIMEType.IMAGE.value, emitter));
    }

    private RequestBody createCountingRequestBody(File file, String mimeType, final FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file, mimeType);
        return new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength) {
                double progress = (1.0 * bytesWritten) / contentLength;
                emitter.onNext(progress);
            }
        });
    }


}