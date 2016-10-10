package bg.hotelmap.hotelmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by Teo on 11-Oct-16.
 */

public class test_barcode extends Activity {

    TextView barcodeResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_barcode);
        Button btn = (Button) findViewById(R.id.scan_barcode);
        barcodeResult = (TextView) findViewById(R.id.barcode_result);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(test_barcode.this,test_scan.class);
                startActivityForResult(intent,0);
            }
        });
    }


    private void ScanBarcode(View v){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeResult.setText("Barcode value is: "+ barcode.displayValue);
                }else{
                    barcodeResult.setText("Barcode not found");
                }
            }
        }
    }
}
