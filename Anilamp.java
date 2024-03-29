import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JSlider;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.util.Hashtable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Anilamp extends JFrame implements ActionListener {

  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  private GLCanvas canvas;
  private Anilamp_GLEventListener glEventListener;
  private final FPSAnimator animator;

  public static void main(String[] args) {
    Anilamp b1 = new Anilamp("Anilamp");
    b1.getContentPane().setPreferredSize(dimension);
    b1.pack();
    b1.setVisible(true);
  }

  public Anilamp(String textForTitleBar) {
    super(textForTitleBar);
    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);
    Camera camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new Anilamp_GLEventListener(camera);
    canvas.addGLEventListener(glEventListener);
    canvas.addMouseMotionListener(new MyMouseInput(camera));
    canvas.addKeyListener(new MyKeyboardInput(camera));
    getContentPane().add(canvas, BorderLayout.CENTER);

    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);
      JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
    menuBar.add(fileMenu);

    JPanel p = new JPanel();
      JButton b = new JButton("Toggle Room Light");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Toggle Lamp Light");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Retract Lamp");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Default Lamp Position");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Random Lamp Position");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("Helicopter Toggle");
      b.addActionListener(this);
      p.add(b);


      JSlider s= new JSlider(1,100,1);
      s.setPaintLabels(true);
      Hashtable<Integer, JLabel> pos = new Hashtable<Integer, JLabel>();
      pos.put(1, new JLabel("Min"));
      pos.put(50, new JLabel("50%"));
      pos.put(100, new JLabel ("Max"));
      s.setLabelTable(pos);
      p.add(s);
      s.addChangeListener(new ChangeListener(){
        public void stateChanged(ChangeEvent e){
          glEventListener.setHeliHeight(((JSlider)e.getSource()).getValue()) ;
        }
      });


    this.add(p, BorderLayout.SOUTH);





    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        animator.stop();
        remove(canvas);
        dispose();
        System.exit(0);
      }
    });
    animator = new FPSAnimator(canvas, 60);
    animator.start();
  }
  public void actionPerformed(ActionEvent e) {
    if(e.getActionCommand().equalsIgnoreCase("Retract Lamp")) {
      glEventListener.lampRetract();
    }else if(e.getActionCommand().equalsIgnoreCase("Default Lamp Position")){
      glEventListener.lampDefault();
    }else if(e.getActionCommand().equalsIgnoreCase("Random Lamp Position")){
      glEventListener.lampRandom();
    }else if(e.getActionCommand().equalsIgnoreCase("Helicopter Toggle")){
      glEventListener.heliToggle();
    }else if(e.getActionCommand().equalsIgnoreCase("Toggle Room Light")){
      glEventListener.lightToggle();
    }else if(e.getActionCommand().equalsIgnoreCase("Toggle Lamp Light")){
      glEventListener.lampToggle();
    }else if(e.getActionCommand().equalsIgnoreCase("quit")){
      System.exit(0);
    }
  }



class MyKeyboardInput extends KeyAdapter  {
  private Camera camera;

  public MyKeyboardInput(Camera camera) {
    this.camera = camera;
  }

  public void keyPressed(KeyEvent e) {
    Camera.Movement m = Camera.Movement.NO_MOVEMENT;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
      case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
      case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
      case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
      case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
      case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
    }
    camera.keyboardInput(m);
  }
}

class MyMouseInput extends MouseMotionAdapter {
  private Point lastpoint;
  private Camera camera;

  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }

    /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */
  public void mouseDragged(MouseEvent e) {
    Point ms = e.getPoint();
    float sensitivity = 0.001f;
    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
    //System.out.println("dy,dy: "+dx+","+dy);
    if (e.getModifiers()==MouseEvent.BUTTON1_MASK)
      camera.updateYawPitch(dx, -dy);
    lastpoint = ms;
  }

  /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */
  public void mouseMoved(MouseEvent e) {
    lastpoint = e.getPoint();
  }
}
}
