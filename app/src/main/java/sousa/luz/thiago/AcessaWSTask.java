package sousa.luz.thiago;

import android.os.AsyncTask;

public class AcessaWSTask extends AsyncTask <String,Integer,String>
{
    @Override
    protected String doInBackground(String... strings)
    {
        String xml=AcessaWS.consumir(strings[0]);
        return xml;
    }
}
