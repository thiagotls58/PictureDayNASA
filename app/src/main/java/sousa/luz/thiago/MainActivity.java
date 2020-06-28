package sousa.luz.thiago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class MainActivity extends AppCompatActivity {

    private TextView tvDateOfPicture, tvTitleOfPicture, tvCopyright, tvExplanation;
    private ImageView ivPictureOfDay;
    private Calendar calendar;
    private final String apiKey = "bTHrDF5ekeqN0xJqhr6cqfhZlIigMkvLV5YgumZS";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String data;
        switch (item.getItemId()) {
            case R.id.iNextDay:
                calendar.add(Calendar.DATE, 1);
                if (Calendar.getInstance().compareTo(calendar) < 0) {
                    Toast.makeText(getApplicationContext(), "Erro: Não é possível obter a foto de amanhã", Toast.LENGTH_SHORT).show();
                    calendar.add(Calendar.DATE, -1);
                } else {
                    data = dateFormat.format(calendar.getTime());
                    getPictureOfDay(data);
                }
                break;
            case R.id.iPreviousDay:
                calendar.add(Calendar.DATE, -1);
                data = dateFormat.format(calendar.getTime());
                getPictureOfDay(data);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDateOfPicture = findViewById(R.id.tvDateOfPicture);
        tvTitleOfPicture = findViewById(R.id.tvTitleOfPicture);
        tvCopyright = findViewById(R.id.tvCopyright);
        tvExplanation = findViewById(R.id.tvExplanation);
        ivPictureOfDay = findViewById(R.id.ivPictureOfDay);

        calendar = Calendar.getInstance();
        String data = dateFormat.format(calendar.getTime());
        getPictureOfDay(data);
    }

    private void getPictureOfDay(String data) {
        if (data != null && data != "") {
            String url = "https://api.nasa.gov/planetary/apod?api_key="+this.apiKey+"&date="+data;
            AcessaWSTask task = new AcessaWSTask();
            String json;
            Gson gson;
            ApiNasa apiNasa;
            try {
                json = task.execute(url).get();
                gson = new Gson();
                apiNasa = gson.fromJson(json, ApiNasa.class);
                showInformation(apiNasa);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Não foi possível as informações", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Erro com a data", Toast.LENGTH_SHORT).show();
        }

    }

    private void showInformation(ApiNasa apiNasa) {
        if (apiNasa != null) {
            tvDateOfPicture.setText(dateFormat.format(apiNasa.getDate()));
            tvTitleOfPicture.setText(apiNasa.getTitle());
            tvCopyright.setText(apiNasa.getCopyright());
            tvExplanation.setText(apiNasa.getExplanation());
            tvExplanation.setMovementMethod(new ScrollingMovementMethod()); // Scroll na TextView

            Picasso.get().load(apiNasa.getHdurl()).priority(HIGH).error(R.mipmap.ic_launcher).into(ivPictureOfDay, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError(Exception e) {
                    Toast.makeText(getApplicationContext(), "Não foi possível carregar a imagem", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            tvDateOfPicture.setText("Not defined");
            tvTitleOfPicture.setText("Not defined");
            tvCopyright.setText("Not defined");
            tvExplanation.setText("Not defined");
        }
    }


}