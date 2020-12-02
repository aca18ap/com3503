import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Anilamp_GLEventListener implements GLEventListener {

  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
  //private Table table;

  public Anilamp_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(2f,2f,2f));
  }


  /* Initialisation */
  public void init(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }


  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }


  /**** Clean up memory - add as you go ****/

  public void dispose(GLAutoDrawable drawable){
    GL3 gl = drawable.getGL().getGL3();
    // model.dispose(gl)  <--- for every model
    //floor.dispose(gl);
    light.dispose(gl);
    //table.dispose(gl);
  }


  /**** SCENE ****/

  /* declarations here*/

  private Light light;
  private Model floor, window, wall, tableTop, tableLeg;
  private SGNode tableRoot;
  private Table table;
  private Room room;
  private Lamp lamp;
  private Heli heli;
  private Skybox skybox;
  private Pinboard pinboard;

  public void initialise(GL3 gl){
    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/wall.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/floor.jpg");
    light = new Light(gl);
    light.setCamera(camera);
    //floor = initFloor(gl, textureId1);
    //wall = initWall(gl, textureId0);
    //window = initWindow(gl, textureId0);
    room = new Room(gl, camera, light);
    table = new Table(gl, camera, light);
    lamp = new Lamp(gl, camera, table);
    heli = new Heli(gl, camera, light, table);
    skybox = new Skybox(gl,camera,light);
    pinboard = new Pinboard(gl, camera, light);

  }

  public void render(GL3 gl){
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(0,16,0);
    light.render(gl);

    room.render(gl);
    skybox.render(gl);
    table.draw(gl);
    lamp.draw(gl);

    heli.draw(gl);
    pinboard.draw(gl);

    //lamp.updateLowerArmZ();
    //lamp.updateLowerArmY();
    //lamp.updateUpperArmZ();

    if (!(getSeconds()-startTime > 10)){
      lamp.retractLamp();
      heli.heliFloat(2f);
    }else{
      //lamp.randomLampPosition();
      lamp.setLampPosition(40f, -20f, 50f);
      heli.heliLand();

    }
    //lamp.changeLampPosition(Math.random())

  }




  public void heliTakeOff(){
    heli.heliFloat(2f);
  }

  public void heliLand(){
    heli.heliLand();
  }

  public void lampRetract(){
    lamp.retractLamp();
  }

  public void lampRandom(){
    lamp.randomLampPosition();
  }

  public void lampDefault(){
    lamp.setLampPosition(-65f,130f,30f);
  }


  private double startTime;

  private double getSeconds(){
    return System.currentTimeMillis()/1000.0;
  }


}
