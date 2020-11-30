package com.example.notes;



 import android.content.SharedPreferences;
 import androidx.appcompat.app.AppCompatActivity;
  import android.os.Bundle;
 import android.view.View;
  import android.widget.EditText;
 import android.widget.Toast;

public class denglu extends AppCompatActivity {
    EditText et_1;
     EditText et_2;

           @Override
   protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_enter);

      et_1=(EditText)findViewById(R.id.edit1);
       et_2=(EditText)findViewById(R.id.edit2);

       SharedPreferences sp=getSharedPreferences("yhm", MODE_PRIVATE);

      String str=sp.getString("save",null);

   et_1.setText(str);

     String str1=sp.getString("save1",null);

       et_2.setText(str1);
    }

   public void bt_OnClick(View v)
  {
      EditText et_1=(EditText)findViewById(R.id.edit1);
     et_2=(EditText)findViewById(R.id.edit2);

        String string=et_1.getText().toString();
     String string1=et_2.getText().toString();

      SharedPreferences sharedPreferences=getSharedPreferences("yhm",MODE_PRIVATE);

     SharedPreferences.Editor editor=sharedPreferences.edit();

     editor.putString("save",string);
     editor.putString("save1",string1);

     editor.commit();

  }
}

.java