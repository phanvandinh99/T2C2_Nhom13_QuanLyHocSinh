package com.example.studentscoremanagement;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscoremanagement.Adapter.ReportItemAdapter;
import com.example.studentscoremanagement.Model.DiemHocSinhDTO;
import com.example.studentscoremanagement.Model.DiemMonHocDTO;
import com.example.studentscoremanagement.Model.HocSinh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    TextView tvClassId, tvTeacherName;
    Button btnExportPDF;
    ListView lvReport;

    ArrayList<DiemHocSinhDTO> diemHocSinhDTOS;
    ReportItemAdapter reportItemAdapter;
    DBHelper dbHelper;

    String idClass;
    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    //Điểm bắt đầu vẽ bảng tính theo chiều dọc
    int y0=200;
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setControl();
        setEvent();
    }

    private void setEvent() {
        getIdClass();
        dbHelper = new DBHelper(this);
        ArrayList<HocSinh> hocSinhs = new ArrayList<>();
        Cursor cursor = dbHelper.GetData("SELECT * FROM " + DBHelper.TB_HOCSINH + " WHERE " + DBHelper.COL_HOCSINH_MALOP + "='"+idClass+"'", new String[]{String.valueOf(DBHelper.COL_HOCSINH_MAHOCSINH)});
        cursor.moveToFirst();
        do {
            HocSinh hocSinh = new HocSinh();
            hocSinh.setMaHS(cursor.getString(0));
            hocSinh.setHo(cursor.getString(1));
            hocSinh.setTen(cursor.getString(2));
            hocSinh.setPhai(cursor.getString(3));
            hocSinh.setNgaySinh(cursor.getString(4));
            hocSinh.setMaLop(cursor.getString(5));
            hocSinhs.add(hocSinh);
        } while (cursor.moveToNext());

        diemHocSinhDTOS = new ArrayList<>();
        for (HocSinh hs : hocSinhs) {
            diemHocSinhDTOS.add(hs.getStudentScore(dbHelper));
        }
        reportItemAdapter = new ReportItemAdapter(ReportActivity.this, R.layout.report_item, diemHocSinhDTOS);
        lvReport.setAdapter(reportItemAdapter);

        setEventExport();
    }

    private void getIdClass() {
        Intent intent=getIntent();
        idClass=intent.getStringExtra(DSSV.CLASS_ID);
        String teacherName=intent.getStringExtra(DSSV.TEACHER_NAME);
        tvClassId.setText(idClass);
        tvTeacherName.setText(teacherName);
    }

    private void setControl() {
        lvReport = findViewById(R.id.lvReport);
        tvClassId = findViewById(R.id.tvClassId);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        btnExportPDF = findViewById(R.id.btnExportPDF);
    }

    private void setEventExport() {
        // initializing  variables.
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 50, 50, false);

        // checking our permissions.
        if (checkPermission()) {
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        btnExportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF();
            }
        });
    }


    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight*(diemHocSinhDTOS.size()/2), 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 30, 20, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("Quản lý điểm sinh viên", 85, 50, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        title.setTextSize(26);
        title.setColor(Color.RED);
        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        title.setFakeBoldText(true);
        canvas.drawText("ĐIỂM TỔNG KẾT HỌC SINH.", 396, 120, title);

        title.setColor(Color.BLACK);
        title.setTextSize(15);
        title.setFakeBoldText(false);
        canvas.drawText("Lớp:", 200, 160, title);
        canvas.drawText("Giáo viên chủ nhiệm:", 500, 160, title);

        title.setFakeBoldText(true);
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        canvas.drawText(idClass, 235, 160, title);
        canvas.drawText(tvTeacherName.getText().toString(), 625, 160, title);


        for(int i=0;i<diemHocSinhDTOS.size();i++){
            drawTable(canvas,title,5,4,diemHocSinhDTOS.get(i),i+1);
        }


        //canvas.drawText("This is sample document which we have created.", 396, 560, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/StudentScoreMn";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String fileName="Báo-cáo-điểm-lớp-"+tvClassId.getText()+"_"+dateFormat.format(date)+".pdf";
        File file = new File(dir, fileName);

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ReportActivity.this, "Xuất PDF thành công vào: \n"+path, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
        //viewPdf(path,fileName);
    }
    private void drawRectangle(Canvas canvas, Paint paint,int x, int y, int width, int height, String text){
        canvas.drawLine(x,y,x+width,y,paint);
        canvas.drawLine(x,y,x,y+height,paint);
        canvas.drawLine(x+width,y,x+width,y+height,paint);
        canvas.drawLine(x,y+height,x+width,y+height,paint);
        canvas.drawText(text,x+width/2-(text.length()/2)*3,y+height/2+5,paint);
    }

    private void drawTable(Canvas canvas, Paint paint, int row, int col, DiemHocSinhDTO diemHocSinhDTO, int stt){
        int x0=100,x=600;
        int y0Temp=y0;
        int width=pagewidth-200,height=25;
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(15);
        paint.setFakeBoldText(false);

        canvas.drawText("STT:"+stt, x0, y0+=height, paint);
        canvas.drawText("Mã học sinh: "+diemHocSinhDTO.getHocSinh().getMaHS(), x0, y0+=height, paint);
        canvas.drawText("Tên: "+diemHocSinhDTO.getHocSinh().getHo()+" "+diemHocSinhDTO.getHocSinh().getTen(), x0, y0+=height, paint);
        canvas.drawText("Giới tính: "+diemHocSinhDTO.getHocSinh().getPhai(), x0, y0+=height, paint);
        canvas.drawText("Ngày sinh: "+diemHocSinhDTO.getHocSinh().getNgaySinh(), x0, y0+=height, paint);
        canvas.drawText("Điểm tổng kết:", x0, y0+=height, paint);

        ArrayList<DiemMonHocDTO> diemMonHocDTOs=diemHocSinhDTO.getDiemMonHocDTOS();


        //Draw header table
        y0 += height;
        paint.setFakeBoldText(true);
        drawRectangle(canvas, paint, x0, y0, width / col, height, "STT");
        drawRectangle(canvas, paint, x0 + (width / col), y0, width / col, height, "Tên MH");
        drawRectangle(canvas, paint, x0 + (width / col) * 2, y0, width / col, height, "Hệ số");
        drawRectangle(canvas, paint, x0 + (width / col) * 3, y0, width / col, height, "Điểm");

        //draw content table
        paint.setFakeBoldText(false);
        float scoreAvg=0;
        for(int i=0;i<diemMonHocDTOs.size();i++)
        {
            y0+=height;
            DiemMonHocDTO diemMonHoc=diemMonHocDTOs.get(i);
            drawRectangle(canvas,paint,x0,y0,width/col,height,String.valueOf(i+1));
            drawRectangle(canvas,paint,x0+(width/col),y0,width/col,height,diemMonHoc.getTenMH());
            drawRectangle(canvas,paint,x0+(width/col)*2,y0,width/col,height,String.valueOf(diemMonHoc.getHeSo()));
            String diem= diemMonHoc.getDiem()==-1?".":String.valueOf(diemMonHoc.getDiem());
            drawRectangle(canvas,paint,x0+(width/col)*3,y0,width/col,height,diem);
            scoreAvg+=diemMonHoc.getDiem()==-1?0:diemMonHoc.getDiem();
        }
        scoreAvg=(float) Math.round((scoreAvg/diemMonHocDTOs.size()) * 10) / 10;
        canvas.drawText("Tổng số môn học: "+diemMonHocDTOs.size(), x0, y0+=height*2, paint);
        canvas.drawText("Điểm trung bình: "+scoreAvg, x0+200, y0, paint);
        y0+=height;

        drawRectangle(canvas, paint, x0-10, y0Temp-10, width +20, y0-y0Temp, "");
        y0+=height*2;
    }

    // Method for opening a pdf file
    private void viewPdf(String directory, String file) {

        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/StudentScoreMn/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(pdfIntent, "Open File");
        try {
            startActivity(intent );
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Không thể đọc file PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(ReportActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}