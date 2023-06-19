package com.example.pf_ia;
/*
*
*
* @author guiom
*
*
 */

import android.Manifest;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Declaracion de variables universales
    Button select, camera;
    ImageView imageView;
    Bitmap bitmap, BitmapR, BitmapV, BitmapB,b;
    Mat mat, img;
    int SELECT_CODE = 100, CAMERA_CODE = 102;


    /*
     *
     *
     *
     *
     * CARGA DE IMAGENES
     *
     *
     *
     *
     */
    //Implementacion del metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Carga el contenido visual de la aplicacion
        setContentView(R.layout.activity_main);


        //Condicionador que verifica si se ha cargado correctamente las librerias de openCV
        if (OpenCVLoader.initDebug()) Log.d("LOADED", "succes");
        else Log.d("LOADED", "err");

        //Lama al metodo getPermision
        getPermission();

        //Encuentra y asigna los botones corresondientes a su diseo
        //mediante su identificador en su archivo XML
        camera = findViewById(R.id.camera);
        select = findViewById(R.id.select);
        imageView = findViewById(R.id.imageView);


        //Evento que escucha cuando el boton select es presionado por el usuario
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_CODE);

            }

        });
        //Evento que escucha cuando el boton camera es presionado por el usuario
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_CODE);
            }
        });

    }

    //Implementacion del metodo onActivityResult
    //Se utiliza para seleccionar la imagen desde la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Verificamos que la seleccion de la imagen sea correcto
        if (requestCode == SELECT_CODE && data != null) {
            try {
                //Se obtiene la imagen seleccionada y se muestra en el imageView
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                imageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //Verificamos que la captura de la imagen desde la camara sea correcto
        if (requestCode == CAMERA_CODE && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

        }

    }

    //Implementacion del metodo getPermission
    //Se ustilizara para solicitar los permisos del uso de la camara del dispositivo movil
    void getPermission() {
        //Verificamos si los permisos no han sido consedidos
        //En caso contrario se solicitaran
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 102);
        }

    }


    //Este metodo convierte un objeto Bitmap a Mat
    private void convertirBitmapAMat(Bitmap bitmap) {
        mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
    }

    //Este metodo transforma una imagen BGR a HSV
    private Mat convertirBGRaHSV(Mat bgrImg) {
        Mat hsvImg = new Mat();
        Imgproc.cvtColor(bgrImg, hsvImg, Imgproc.COLOR_BGR2HSV);
        return hsvImg;
    }

    /*
     *
     *
     *
     *
     * VISUALIZACION DE LOS CANALES RGB
     *
     *
     *
     *
     */
    //Este metodo separa el canal rojo de una imagen
    public void canalRojo(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);

        //Utilizando la funcion extractChannel de la calse Core
        //Extraemos el canal rojo de la matriz imgR y la almacenamos en canalR
        Core.extractChannel(mat, mat, 0);

        //Convertimos la imagen con el canal rojo de escala de grises a RGB
        //Y lo almacenamos en la variable rgbCanalR
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);
        BitmapR = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);

        //Almacenamos en un bitmap la imagen con el canal rojo
        Utils.matToBitmap(mat, BitmapR);

        ////Aqui se muestra la imagen final en el imageView
        imageView.setImageBitmap(BitmapR);

    }

    //Este metodo separa el canal verde de una imagen
    public void canalVerde(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);

        //Utilizando la funcion extractChannel de la calse Core
        //Extraemos el canal de la matriz
        Core.extractChannel(mat, mat, 1);

        //Convertimos la imagen con el canal, de escala de grises a RGB
        //Y lo almacenamos en otra variable
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);
        BitmapV = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);

        //Almacenamos en un bitmap la imagen con el canal
        Utils.matToBitmap(mat, BitmapV);

        ////Aqui se muestra la imagen final en el imageView
        imageView.setImageBitmap(BitmapV);

    }

    //Este metodo separa el canal azul de una imagen
    public void canalAzul(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);

        //Utilizando la funcion extractChannel de la calse Core
        //Extraemos el canal de la matriz
        Core.extractChannel(mat, mat, 2);

        //Convertimos la imagen con el canal, de escala de grises a RGB
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);
        BitmapB = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);

        //Almacenamos en un bitmap la imagen con el canal
        Utils.matToBitmap(mat, BitmapB);

        ////Aqui se muestra la imagen final en el imageView
        imageView.setImageBitmap(BitmapB);

    }

    /*
     *
     *
     *
     *
     * CONVERSION A ESCALA DE GRISES
     *
     *
     *
     *
     */
    //Este metodo convierte una imagen en escala de grises
    public void escalaDeGrises(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);

        //Utilizando la clase Imgproc para convertir la imagen a escala de grises
        //La funcion cvtColor se utiliza para cambiar el epacio de color de la imagen
        //Almacenando el resultado en la misma matriz mat y remplazando la version original
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

        //Creamos un nuevo objeto bitmap con el nombre grayBitmap para almacenar la imagen en escala de grises
        Bitmap grayBitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);

        //Volvemos a covertir de mat a bitmap pero esta vez se almacenara en la variable grayBitmap
        Utils.matToBitmap(mat, grayBitmap);

        //Aqui se muestra la imagen convertida en el imageView
        imageView.setImageBitmap(grayBitmap);
    }

    /*
     *
     *
     *
     *
     * IMAGEN BINARIA
     *
     *
     *
     *
     */
    //Este metodo convierne una imagen en escala binaria
    public void convertirABinaria(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);

        //Utilizando la clase Imgproc para convertir la imagen a escala de grises
        //La funcion cvtColor se utiliza para cambiar el epacio de color de la imagen
        //Almacenando el resultado en la misma matriz mat y remplazando la version original
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

        //Le aplicamos una umbralizacin binaria la matriz utilizando OTSU
        //Este metodo separara los pixeles en dos categorias: negro(0) y blanco(255)
        Imgproc.threshold(mat, mat, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Bitmap binaryBitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, binaryBitmap);

        imageView.setImageBitmap(binaryBitmap);
    }


    /*
     *
     *
     *
     *
     * SEGMENTACION POR COLORES
     *
     *
     *
     *
     */
    public void coloresAmarillo(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);
        img = convertirBGRaHSV(mat);

        Scalar min_amarillo = new Scalar(80, 100, 100);
        Scalar max_amarillo = new Scalar(100, 255, 255);

        Mat mascara_amarillo = new Mat();
        Core.inRange(img, min_amarillo, max_amarillo, mascara_amarillo);

        Mat objetos_amarillos = new Mat();
        Core.bitwise_and(mat, mat, objetos_amarillos, mascara_amarillo);

        Bitmap resultBitmapMagenta = Bitmap.createBitmap(objetos_amarillos.cols(), objetos_amarillos.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(objetos_amarillos, resultBitmapMagenta);
        imageView.setImageBitmap(resultBitmapMagenta);
    }

    public void coloresCyan(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);
        img = convertirBGRaHSV(mat);

        Scalar min_cyan = new Scalar(20, 100, 100);
        Scalar max_cyan = new Scalar(40, 255, 255);

        Mat mascara_cyan = new Mat();
        Core.inRange(img, min_cyan, max_cyan, mascara_cyan);

        Mat objetos_cyan = new Mat();
        Core.bitwise_and(mat, mat, objetos_cyan, mascara_cyan);

        Bitmap resultBitmapCyan = Bitmap.createBitmap(objetos_cyan.cols(), objetos_cyan.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(objetos_cyan, resultBitmapCyan);
        imageView.setImageBitmap(resultBitmapCyan);
    }

    public void coloresBlanco(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);
        img = convertirBGRaHSV(mat);

        Scalar min_cyan = new Scalar(0, 0, 200);
        Scalar max_cyan = new Scalar(180, 30, 255);

        Mat mascara_blanco = new Mat();
        Core.inRange(img, min_cyan, max_cyan, mascara_blanco);

        Mat objetos_blancos = new Mat();
        Core.bitwise_and(mat, mat, objetos_blancos, mascara_blanco);

        Bitmap resultBitmapCyan = Bitmap.createBitmap(objetos_blancos.cols(), objetos_blancos.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(objetos_blancos, resultBitmapCyan);
        imageView.setImageBitmap(resultBitmapCyan);
    }

    /*
     *
     *
     *
     *
     * DETECCION DE CONTORNOS
     *
     *
     *
     *
     */
    public void detectarContornos(View view) {
        //Lamado del metodo convertirBitmapAMat
        convertirBitmapAMat(bitmap);

        Mat grayImage = new Mat();
        Imgproc.cvtColor(mat, grayImage, Imgproc.COLOR_BGR2GRAY);
        Mat binaryImage = new Mat();
        Imgproc.threshold(grayImage, binaryImage, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat resultImage = mat.clone();
        Imgproc.drawContours(resultImage, contours, -5, new Scalar(0, 255, 0), 2);
        Bitmap resultBitmap = Bitmap.createBitmap(resultImage.cols(), resultImage.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(resultImage, resultBitmap);
        imageView.setImageBitmap(resultBitmap);

    }

    /*
     *
     *
     *
     *
     * OPCIONES DE EDICION
     *
     *
     *
     *
     */
    public void edicionRotar(View view) {

        // Especifica el ángulo de rotación deseado (en grados)
        float angulo = 90.0f;

        Matrix matrix = new Matrix();
        matrix.postRotate(angulo);

        //Se crea una instancia nueva para almacenar el bitmap de la imagen rotada
        //pasando las coordenadas del bitmap original y la matriz de transformacion
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // Muestra el Bitmap rotado en el ImageView
        imageView.setImageBitmap(bitmap);
    }

    public void restaurarImagenOriginal(View view) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}








