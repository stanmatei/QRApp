package matei.mple.com.qrapp;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.activation.*;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class AddActivity extends AppCompatActivity {
    String TAG = "GenerateQRCode";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    DatabaseReference db;
    Button btn;
    EditText ed;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        btn=(Button)findViewById(R.id.emailbtn);
        ed=(EditText)findViewById(R.id.email);
        db= FirebaseDatabase.getInstance().getReference().child("ppl");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String inputText=ed.getText().toString();
                DatabaseReference push = db.push();
                final String push_id = push.getKey();
                qrgEncoder = new QRGEncoder(push_id, null, QRGContents.Type.TEXT,115);
                try {
                    // Getting QR-Code as Bitmap
                    bitmap = qrgEncoder.encodeAsBitmap();
                    final String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                    OutputStream fOut = null;
                    Integer counter = 0;
                    // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    File folder = new File(path+"/mytestboi/");
                    if(!folder.exists()){
                        folder.mkdir();
                    }
                    File file = new File(path+"/mytestboi/", "yeahboi.png");
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close();
                    sendMail(path+"/mytestboi/","yeahboi.png", inputText);




                    Map map=new HashMap();
                    map.put(push_id,0);
                    db.updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            ed.getText().clear();



                        }
                    });


                } catch (WriterException e) {
                    Log.v(TAG, e.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
   /* private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"testtest1.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    */
   private void sendMail(String path,String fileName1, String emailText) {

       final String username = "potus46official@gmail.com";
       final String password = "Fagalas1";

       Properties props = new Properties();
       props.put("mail.smtp.auth", true);
       props.put("mail.smtp.starttls.enable", true);
       props.put("mail.smtp.host", "smtp.gmail.com");
       props.put("mail.smtp.port", "587");

       Session session = Session.getInstance(props,
               new javax.mail.Authenticator() {
                   protected PasswordAuthentication getPasswordAuthentication() {
                       return new PasswordAuthentication(username, password);
                   }
               });

       try {

           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress("potus46official@gmail.com"));
           message.setRecipients(Message.RecipientType.TO,
                   InternetAddress.parse(emailText));
           message.setSubject("Testing Subject");
           message.setText("PFA");

           MimeBodyPart messageBodyPart = new MimeBodyPart();

           Multipart multipart = new MimeMultipart();

           messageBodyPart = new MimeBodyPart();
           String file = path;
           String fileName = fileName1;
           DataSource source = new FileDataSource(file+fileName);
           messageBodyPart.setDataHandler(new DataHandler(source));
           messageBodyPart.setFileName(path+fileName);
           multipart.addBodyPart(messageBodyPart);

           message.setContent(multipart);

           //System.out.println("Sending");


           Transport.send(message);
           Toast.makeText(AddActivity.this,"sent",Toast.LENGTH_LONG).show();

           //System.out.println("Done");

       } catch (MessagingException e) {
           e.printStackTrace();
       }
   }

}
