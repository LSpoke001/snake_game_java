import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16; //Размер одного квадратика в пикселях
    private final int ALL_DOTS = 400; //Колличество возможно-уместимых квадратиков

    private Image dot;
    private Image apple;

    private int appleX; //координаты яблока
    private int appleY;

    private int[] x = new int[ALL_DOTS]; // координаты змейки
    private int[] y = new int[ALL_DOTS];

    private int dots; // размер змейки на текущий момент

    private Timer timer;

    private boolean left = false;
    private boolean right = true;
    private boolean top = false;
    private boolean bottom = false;
    private boolean inGame = true;

    public GameField(){
        setBackground(Color.BLACK);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame(){
        dots = 3;
        for(int i = 0; i < dots; i++){
            x[i] = 48 - i*DOT_SIZE; //начальные значения для каждого X и Y
            y[i] = 48;
        }
        timer = new Timer(250,this);
        timer.start();
        createApple();
    }
    public void createApple(){
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }
    private void loadImages(){
        ImageIcon iiApple = new ImageIcon("apple.png");
        apple = iiApple.getImage();

        ImageIcon iiDot = new ImageIcon("cube.png");
        dot = iiDot.getImage();
    }

    public void checkApple(){
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            createApple();
        }
    }
    public void checkCollision(){
        for(int i = dots; i>0;i--){
            if(i>4 && x[0] == x[i] && y[0]==y[i]){
                inGame= false;
            }
        }
        if(x[0] > SIZE){
            inGame= false;
        }
        if(x[0] < 0){
            inGame= false;
        }
        if(y[0] > SIZE){
            inGame= false;
        }
        if(y[0] < 0){
            inGame= false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(apple, appleX, appleY, this);
            for(int i = 0; i < dots; i++){
                g.drawImage(dot, x[i], y[i], this);
            }
        }else{
            String gameOver = "GAME OVER";
            g.setColor(Color.WHITE);
            g.drawString(gameOver, 140, SIZE/2);
        }
    }

    public void move(){
        for (int i = dots; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){
            x[0]-= DOT_SIZE;
        }
        if(right){
            x[0]+= DOT_SIZE;
        }
        if(top){
            y[0]-= DOT_SIZE;
        }
        if(bottom){
            y[0]+= DOT_SIZE;
        }
    }
    //  метод который вызывается каждый раз, когда будет тикать таймер, каждые 250 милсек
    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint(); //вызывает paint component, а он отрисовывает все компоненты
    }
    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                top = false;
                bottom = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                top = false;
                bottom = false;
            }
            if(key == KeyEvent.VK_UP && !bottom){
                top = true;
                left = false;
                right = false;
            }
            if(key == KeyEvent.VK_DOWN && !top){
                bottom = true;
                left = false;
                right = false;
            }
        }
    }
}
