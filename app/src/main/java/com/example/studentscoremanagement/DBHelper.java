package com.example.studentscoremanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studentscoremanagement.Model.TaiKhoan;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_NAME="dbStudentScoreMn.db";
    private static int DB_VERSION=1;

    //bang Tai Khoan
    public static final String TB_TAIKHOAN="tbTaiKhoan";
    public static final String COL_TAIKHOAN_TEN="taiKhoan_ten";
    public static final String COL_TAIKHOAN_MATKHAU="taiKhoan_matKhau";
    public static final String COL_TAIKHOAN_SDT="taiKhoan_sdt";
    public static final String COL_TAIKHOAN_ANH="taiKhoan_anh";

    //bang Lop
    public static final String TB_LOP="tbLop";
    public static final String COL_LOP_MALOP="lop_maLop";
    public static final String COL_LOP_CHUNHIEM="lop_chuNhiem";

    //bang Hoc Sinh
    public static final String TB_HOCSINH="tbHocSinh";
    public static final String COL_HOCSINH_MAHOCSINH="hocSinh_maHocSinh";
    public static final String COL_HOCSINH_HO="hocSinh_ho";
    public static final String COL_HOCSINH_TEN="hocSinh_ten";
    public static final String COL_HOCSINH_PHAI="hocSinh_phai";
    public static final String COL_HOCSINH_NGAYSINH="hocSinh_ngaySinh";
    public static final String COL_HOCSINH_MALOP="hocSinh_maLop";

    //bang Mon Hoc
    public static final String TB_MONHOC="tbMonHoc";
    public static final String COL_MONHOC_MAMONHOC="monHoc_maMonHoc";
    public static final String COL_MONHOC_TENMONHOC="monHoc_tenMonHoc";
    public static final String COL_MONHOC_HESO="monHoc_heSo";

    //bang Diem
    public static final String TB_DIEM="tbDiem";
    public static final String COL_DIEM_MAHOCSINH="diem_maHocSinh";
    public static final String COL_DIEM_MAMONHOC="diem_maMonHoc";
    public static final String COL_DIEM_DIEM="diem_diem";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String scripTBTaiKhoan="CREATE TABLE "+ TB_TAIKHOAN+"("+
                COL_TAIKHOAN_TEN+" TEXT PRIMARY KEY,"+
                COL_TAIKHOAN_MATKHAU+" TEXT NOT NULL," +
                COL_TAIKHOAN_SDT+" TEXT NOT NULL," +
                COL_TAIKHOAN_ANH+" BLOB)";

        String scripTBLop="CREATE TABLE "+ TB_LOP+"("+
                COL_LOP_MALOP+" TEXT PRIMARY KEY,"+
                COL_LOP_CHUNHIEM+" TEXT NOT NULL)";

        String scripTBHocSinh = "CREATE TABLE " + TB_HOCSINH + "(" +
                COL_HOCSINH_MAHOCSINH + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COL_HOCSINH_HO + " TEXT NOT NULL," +
                COL_HOCSINH_TEN + " TEXT NOT NULL," +
                COL_HOCSINH_PHAI + " TEXT NOT NULL," +
                COL_HOCSINH_NGAYSINH + " TEXT," +
                COL_HOCSINH_MALOP + " TEXT," +
                "hocSinh_tenDangNhap TEXT UNIQUE," +
                "hocSinh_matKhau TEXT," +
                "FOREIGN KEY (" + COL_HOCSINH_MALOP + ") REFERENCES " + TB_LOP + "(" + COL_LOP_MALOP + "))";

        String scripTBMonHoc="CREATE TABLE "+ TB_MONHOC+"("+
                COL_MONHOC_MAMONHOC+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                COL_MONHOC_TENMONHOC+" TEXT NOT NULL,"+
                COL_MONHOC_HESO+" INTEGER)";

        String scripTBDiem="CREATE TABLE "+ TB_DIEM+"("+
                COL_DIEM_MAHOCSINH+" TEXT NOT NULL,"+
                COL_DIEM_MAMONHOC+" TEXT NOT NULL,"+
                COL_DIEM_DIEM+ " REAL,"+
                "PRIMARY KEY ("+COL_DIEM_MAHOCSINH+", "+COL_DIEM_MAMONHOC+"))";

        //Execute Create query
        db.execSQL(scripTBTaiKhoan);
        db.execSQL(scripTBLop);
        db.execSQL(scripTBHocSinh);
        db.execSQL(scripTBMonHoc);
        db.execSQL(scripTBDiem);

        //Execute Create Data
        addDataDefault(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_TAIKHOAN);
        db.execSQL("DROP TABLE IF EXISTS " + TB_LOP);
        db.execSQL("DROP TABLE IF EXISTS " + TB_HOCSINH);
        db.execSQL("DROP TABLE IF EXISTS " + TB_MONHOC);
        db.execSQL("DROP TABLE IF EXISTS " + TB_DIEM);

        onCreate(db);
    }

    //Truy vấn không trả về kq (update)
    public void QueryData(String sql){
        SQLiteDatabase database =getWritableDatabase();
        database.execSQL(sql);
        database.close();
    }

    //Truy vấn trả về kết quả
    public Cursor GetData(String sql, String[] strings){
        SQLiteDatabase database =getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void themTK(TaiKhoan taiKhoan) {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TAIKHOAN_TEN, taiKhoan.getTenTaiKhoan());
        values.put(COL_TAIKHOAN_MATKHAU, taiKhoan.getMatKhau());
        values.put(COL_TAIKHOAN_SDT, taiKhoan.getSdt());
        db.insert(TB_TAIKHOAN, null, values);
        db.close();
    }


    private void addDataDefault(SQLiteDatabase database){
        database.execSQL("INSERT INTO "+TB_TAIKHOAN+" VALUES ('Nam','123456','0329385635',null)");
        database.execSQL("INSERT INTO "+TB_TAIKHOAN+" VALUES ('GiaHuy','123456','0347360427',null)");
        database.execSQL("INSERT INTO "+TB_TAIKHOAN+" VALUES ('TranHuy','123456','0334704530',null)");
        database.execSQL("INSERT INTO "+TB_TAIKHOAN+" VALUES ('vu','123456','0327551251',null)");
        database.execSQL("INSERT INTO "+TB_TAIKHOAN+" VALUES ('sang','123456','0933545121',null)");

        database.execSQL("INSERT INTO "+TB_LOP+" VALUES ('12A1','Lù Vĩnh Trường')");
        database.execSQL("INSERT INTO "+TB_LOP+" VALUES ('12A2','Nguyễn Hải Nam')");
        database.execSQL("INSERT INTO "+TB_LOP+" VALUES ('12A3','Nguyễn Lương Vũ')");
        database.execSQL("INSERT INTO "+TB_LOP+" VALUES ('12A4','Huỳnh Phước Sang')");
        database.execSQL("INSERT INTO "+TB_LOP+" VALUES ('12A5','Bùi Trọng Nghĩa')");
        database.execSQL("INSERT INTO "+TB_LOP+" VALUES ('12A6','Nguyễn Văn Tuấn')");

        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (1,'Nguyễn Văn','Tèo','Nam','20/20/2005','12A1','1023','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (2,'Nguyễn Văn','Sang','Nam','20/20/2005','12A1','2456','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (3,'Nguyễn Thị','Thắm','Nữ','20/20/2005','12A1','3789','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (4,'Lưu Văn','Tuấn','Nam','20/20/2005','12A1','4912','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (5,'Nguyễn Văn','Trường','Nam','20/20/2005','12A1','5134','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (6,'Phạm Văn','Tú','Nam','20/20/2005','12A1','6278','123456')");

        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (7,'Nguyễn Văn','Tèo','Nam','20/20/2005','12A2','7391','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (8,'Trần Quốc','Trưởng','Nam','20/20/2005','12A2','8415','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (9,'Nguyễn Thị','Thắm','Nữ','20/20/2005','12A2','9527','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (10,'Lưu Văn','Tuấn','Nam','20/20/2005','12A2','1639','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (11,'Nguyễn Văn','Trường','Nam','20/20/2005','12A2','2741','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (12,'Phạm Văn','Tú','Nam','20/20/2005','12A2','3850','123456')");

        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (13,'Nguyễn Văn','Tèo','Nam','20/20/2005','12A3','4962','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (14,'Nguyễn Văn','Vũ','Nam','20/20/2005','12A3','5073','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (15,'Nguyễn Thị','Thắm','Nữ','20/20/2005','12A3','6184','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (16,'Lưu Văn','Tuấn','Nam','20/20/2005','12A3','7295','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (17,'Nguyễn Vĩnh','Trường','Nam','20/20/2005','12A3','8306','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (18,'Phạm Văn','Tú','Nam','20/20/2005','12A3','9417','123456')");

        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (19,'Nguyễn Văn','Tèo','Nam','20/20/2005','12A4','1528','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (20,'Nguyễn Văn','Tý','Nam','20/20/2005','12A4','2639','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (21,'Nguyễn Thị','Thắm','Nữ','20/20/2005','12A4','3740','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (22,'Lưu Văn','Tuấn','Nam','20/20/2005','12A4','4851','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (23,'Nguyễn Văn','Tèo','Nam','20/20/2005','12A4','5962','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (24,'Phạm Văn','Tú','Nam','20/20/2005','12A4','6073','123456')");

        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (25,'Nguyễn Văn','Tèo','Nam','20/20/2005','12A5','7184','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (26,'Nguyễn Văn','Tào','Nam','20/20/2005','12A5','8295','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (27,'Nguyễn Thị','Vi','Nữ','20/20/2005','12A5','9306','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (28,'Lưu Văn','Tuấn','Nam','20/20/2005','12A5','0417','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (29,'Nguyễn Văn','Tường','Nam','20/20/2005','12A5','1529','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (30,'Phạm Văn','Tú','Nam','20/20/2005','12A5','2630','123456')");

        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (31,'Nguyễn Văn','Tèo','Nam','20/20/2005','12A6','3741','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (32,'Nguyễn Văn','Tý','Nam','20/20/2005','12A6','4852','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (33,'Nguyễn Thị','Lan','Nữ','20/20/2005','12A6','5963','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (34,'Lưu Văn','Tuấn','Nam','20/20/2005','12A6','6074','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (35,'Nguyễn Văn','Trường','Nam','20/20/2005','12A6','7185','123456')");
        database.execSQL("INSERT INTO "+TB_HOCSINH+" VALUES (36,'Phạm Văn','Tú','Nam','20/20/2005','12A6','8296','123456')");

        database.execSQL("INSERT INTO "+TB_MONHOC+" VALUES (1,'Toán','2')");
        database.execSQL("INSERT INTO "+TB_MONHOC+" VALUES (2,'Vật lý','2')");
        database.execSQL("INSERT INTO "+TB_MONHOC+" VALUES (3,'Hóa','2')");
        database.execSQL("INSERT INTO "+TB_MONHOC+" VALUES (4,'Văn','2')");
        database.execSQL("INSERT INTO "+TB_MONHOC+" VALUES (5,'Sử','1')");
        database.execSQL("INSERT INTO "+TB_MONHOC+" VALUES (6,'Địa','1')");
        database.execSQL("INSERT INTO "+TB_MONHOC+" VALUES (7,'GDQP','1')");

        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (1,1,7.5)");
        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (8,1,7.5)");
        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (16,1,7.5)");
        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (24,1,7.5)");
        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (32,1,7.5)");
        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (2,1,7.5)");
        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (9,1,7.5)");
        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (17,1,7.5)");
        database.execSQL("INSERT INTO "+TB_DIEM+" VALUES (33,1,7.5)");
    }
}
