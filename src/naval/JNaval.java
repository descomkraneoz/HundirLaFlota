package naval;



import resources.Imagenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class JNaval extends javax.swing.JFrame {

    Image portada;
    Image tablero;
    //nos dice en que momento estamos del juego, si estamos viendo la portada, si estamos jugando, si estamos poniendo los barcos, disparando etc.
    int nEstado=0;

    //establecer dimensiones del tablero, es importante instanciarlo en esta parte del código para que no de fallo
    int tableroMio[][]=new int[8][8];
    boolean bTableroMio[][]=new boolean[8][8];
    int tableroSuyo[][]=new int[8][8];
    boolean bTableroSuyo[][]=new boolean[8][8];

    int pFila=0;
    int pCol=0;
    int pTam=5;
    int pHor=0;



    //indica si nos hemos salido o no del tablero
    public boolean celdaEstaEnTablero(int f, int c){
        if (f<0) return false;
        if (c<0) return false;
        if (f>=8) return false;
        if (c>=8) return false;
        return true;
    }

    //comprueba si el lugar elegido para colocar el barco es correcto, si no lo es, vuelves a colocarlo

    public boolean puedePonerBarco(int tab[][], int tam, int f, int c, int hor){
        int df=0,dc=0;
        if (hor==1) df=1;
        else dc=1;
        for (int c2=c;c2<=c+tam*dc;c2++){
            for (int f2=f;f2<=f+tam*df;f2++){
                if (!celdaEstaEnTablero(f2, c2)){
                    return false;
                }
            }
        }
        for (int f2=f-1;f2<=f+1+df*tam;f2++){
            for (int c2=c-1;c2<=c+1+dc*tam;c2++){
                if (celdaEstaEnTablero(f2,c2)){
                    if (tab[f2][c2]!=0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //con esta funcion ponemos los 5 barcos de distintos tamaños
    //el orden es del mayor, el de 5 cuadrados, al menor, el de 1 cuadrado
    public void ponerBarco(int tab[][], int tam){

        int f,c,hor;
        do{
            f=(int)(Math.random()*8);
            c=(int)(Math.random()*8);
            hor=(int)(Math.random()*2);
        }while(!puedePonerBarco(tab, tam, f, c, hor));
        int df=0,dc=0;
        if (hor==1) df=1;
        else dc=1;
        for (int f2=f;f2<=f+(tam-1)*df;f2++){
            for (int c2=c;c2<=c+(tam-1)*dc;c2++){
                tab[f2][c2]=tam;
            }
        }
    }

    //cuando se inicie la partida recorremos dos arrays 8x8 y los pone a cero (cero=agua)
    public void iniciarPartida(){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                tableroMio[n][m]=0;
                tableroSuyo[n][m]=0;
                //pongo a falso los boleanos al iniciar la partida
                bTableroMio[n][m]=false;
                bTableroSuyo[n][m]=false;
            }
        }
        //hace que no podamos colocar un barco encima de otro
        for (int tam=5;tam>=1;tam--){
            ponerBarco(tableroSuyo, tam);
        }
        pTam=5;
    }

    //Si el barco se sale de los limites del tablero no deja colocarlo, ya no se hace más pequeño.
    public void rectificarBarcoPoner(){
        int pDF=0;
        int pDC=0;
        if (pHor==1){
            pDF=1;
        }else{
            pDC=1;
        }
        if (pFila+pTam*pDF>=8){
            pFila=7-pTam*pDF;
        }
        if (pCol+pTam*pDC>=8){
            pCol=7-pTam*pDC;
        }
    }

    //le paso unas variables para colocar el barco, para que no deje colocar uno junto a otro
    public boolean puedePonerBarco(){
        return puedePonerBarco(tableroMio, pTam, pFila, pCol, pHor);
    }

    //Nos dice si en un tablero, lo que queda es agua, entonces es victoria
    public boolean victoria(int tab[][], boolean bTab[][]){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                if (bTab[n][m]==false && tab[n][m]!=0){
                    return false;
                }
            }
        }
        return true;
    }

    //cuando dispara la maquina, son casillas aleatorias, al disparar las ponemos a true y son repintadas
    public void dispararEl(){
        int f,c;
        do{
            f=(int)(Math.random()*8);
            c=(int)(Math.random()*8);
        }while(bTableroMio[f][c]==true);
        bTableroMio[f][c]=true;
    }

    //cargamos las imagenes

    public JNaval() {
        Imagenes i =new Imagenes();
        portada=i.cargar("C:\\Users\\desco\\IdeaProjects\\HundirLaFlota\\src\\resources\\Portada.jpg");
        tablero=i.cargar("C:\\Users\\desco\\IdeaProjects\\HundirLaFlota\\src\\resources\\Tablero.jpg");
        initComponents();
        setBounds(0,0,800,600);

        //clase que tiene los metodos vacios, solo implemento los metodos necesarios, para pasar de un estado a otro
        addMouseListener(
                new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        //si pulsas el derecho del raton cambias el horizontal del barco a la hora de colocarlo y viceversa
                        if (e.getModifiers() == MouseEvent.BUTTON3_MASK && nEstado==1){
                            pHor=1-pHor;
                            rectificarBarcoPoner();
                            repaint();
                            return;
                        }
                        if (nEstado==0){
                            nEstado=1;
                            iniciarPartida();
                            repaint();
                        }else if (nEstado==1){
                            if (puedePonerBarco()){
                                int pDF=0;
                                int pDC=0;
                                if (pHor==1){
                                    pDF=1;
                                }else{
                                    pDC=1;
                                }
                                for (int m=pFila;m<=pFila+(pTam-1)*pDF;m++){
                                    for (int n=pCol;n<=pCol+(pTam-1)*pDC;n++){
                                        tableroMio[m][n]=pTam;
                                    }
                                }
                                pTam--;
                                if (pTam==0){
                                    nEstado=2;
                                    repaint();
                                }
                            }
                            //Obtenemos la fila y columna de donde hemos disparado
                        }else if (nEstado==2){
                            int f=(e.getY()-200)/30;
                            int c=(e.getX()-450)/30;
                            if (f!=pFila || c!=pCol){
                                pFila=f;
                                pCol=c;
                                if (celdaEstaEnTablero(f, c)){
                                    if (bTableroSuyo[f][c]==false){
                                        bTableroSuyo[f][c]=true;
                                        //repintamos al disparar
                                        repaint();
                                        //mostramos un mensaje al comprobar si victoria(solo queda agua).
                                        if (victoria(tableroSuyo, bTableroSuyo)){
                                            JOptionPane.showMessageDialog(null, "Has ganado");
                                            //fin de partida, cambio de estado a 0
                                            nEstado=0;
                                        }
                                        dispararEl();
                                        repaint();
                                        if (victoria(tableroMio, bTableroMio)){
                                            JOptionPane.showMessageDialog(null, "Has perdido");
                                            nEstado=0;
                                        }
                                        repaint();
                                    }
                                }
                            }
                        }
                    }
                }
        );
        //obtenemos la posicion x,y de donde esta el mouse en nuestro tablero, sabemos fila y columna, repintamos el barco
        addMouseMotionListener(
                new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int x=e.getX();
                        int y=e.getY();
                        if (nEstado==1 && x>=100 && y>=200 && x<100+30*8 && y<200+30*8){
                            int f=(y-200)/30;
                            int c=(x-100)/30;
                            if (f!=pFila || c!=pCol){
                                pFila=f;
                                pCol=c;
                                rectificarBarcoPoner();
                                repaint();
                            }
                        }
                    }
                }
        );
    }

    public boolean noHayInvisible(int tab[][], int valor, boolean bVisible[][]){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                if (bVisible[n][m]==false){
                    if (tab[n][m]==valor){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //pinta el tablero
    public void pintarTablero(Graphics g, int tab[][], int x, int y, boolean bVisible[][]){
        for (int n=0;n<8;n++){
            for (int m=0;m<8;m++){
                if (tab[n][m]>0 && bVisible[n][m]){
                    //si aciertas un barco se pone amarillo, si esta completamente hundido cambia a rojo
                    g.setColor(Color.yellow);
                    if (noHayInvisible(tab, tab[n][m], bVisible)){
                        g.setColor(Color.red);
                    }
                    g.fillRect(x+m*30, y+n*30, 30, 30);
                }
                //al disparar y fallar, lo marcamos en un color mas claro de agua
                if (tab[n][m]==0 && bVisible[n][m]){
                    g.setColor(Color.cyan);
                    g.fillRect(x+m*30, y+n*30, 30, 30);
                }
                //en mi tablero los barcos se quedan gris cualdo son colocados correctamente
                if (tab[n][m]>0 && tab==tableroMio && !bVisible[n][m]){
                    g.setColor(Color.gray);
                    g.fillRect(x+m*30, y+n*30, 30, 30);
                }
                g.setColor(Color.black);
                //pinta casillas de 30x30
                g.drawRect(x+m*30, y+n*30, 30, 30);
                if (nEstado==1 && tab==tableroMio){
                    int pDF=0;
                    int pDC=0;
                    if (pHor==1){
                        pDF=1;
                    }else{
                        pDC=1;
                    }
                    //pinta de color verde si se encuentra dentro del tablero
                    if (n>=pFila && m>=pCol && n<=pFila+(pTam-1)*pDF && m<=pCol+(pTam-1)*pDC){
                        g.setColor(Color.green);
                        g.fillRect(x+m*30, y+n*30, 30, 30);
                    }
                }
            }
        }
    }

    /**
     * Metodo para pintar el tablero y la portada
     */

    boolean bPrimeraVez=true; //Para cuando se cargue por primera vez
    public void paint(Graphics g){
        if (bPrimeraVez){
            g.drawImage(portada, 0,0,1,1,this);
            g.drawImage(tablero, 0,0,1,1,this);
            bPrimeraVez=false;
        }
        if (nEstado==0){
            g.drawImage(portada, 0, 0, this);
        }else {
            g.drawImage(tablero, 0, 0, this);
            //pinta el tablero en unas posiciones x,y
            pintarTablero(g, tableroMio, 90, 200, bTableroMio);
            pintarTablero(g, tableroSuyo, 435, 200, bTableroSuyo);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JNaval().setVisible(true);
            }
        });
    }


}
