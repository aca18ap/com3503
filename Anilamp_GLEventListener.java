import gmaths.*;
import java.util.Random;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Anilamp_GLEventListener implements GLEventListener {

  private static final boolean DISPLAY_SHADERS = true;
  private Camera camera;
  //private Table table;

  /* declarations here*/

  private Light light, sun;
  private Table table;
  private Room room;
  private Lamp lamp;
  private Heli heli;
  private Skybox skybox;
  private Pinboard pinboard;


  private boolean lampRetracting, lampRandomizing, lampDefaulting;
  private boolean togglePropellers, toggleFloat, toggleLand;
  private boolean roomLight = true, lampLight = false;
  private Vec3 rndV;
  private float heliHeight = 2.4f;   //default helicopter height

  private Vec3 sunPosition = new Vec3(3.0f,-2.0f,-13.0f);
  private double aniStart;
  private Vec3 lightOffV = new Vec3(0.2f,0.2f,0.2f);

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
    gl.glCullFace(GL.GL_BACK);
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
    table.dispose(gl);
    room.dispose(gl);
    lamp.dispose(gl);
    heli.dispose(gl);
    skybox.dispose(gl);
    pinboard.dispose(gl);
    //sun.dispose(gl);

  }


  /**** SCENE ****/




  public void initialise(GL3 gl){
    light = new Light(gl);
    light.setCamera(camera);
    sun = new Light(gl);
    sun.setCamera(camera);
    //light.setPosition(sunPosition);
    room = new Room(gl, camera, light);
    table = new Table(gl, camera, light);
    lamp = new Lamp(gl, camera, light, table);
    heli = new Heli(gl, camera, light, table);
    skybox = new Skybox(gl,camera,sun);
    pinboard = new Pinboard(gl, camera, light);

  }

  public void render(GL3 gl){
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(-1.8f,1.8f,-2f);
    sun.setPosition(13f,9f,15f);
    //light.render(gl);

    //light.render(gl);
    sun.render(gl);
    room.render(gl);
    skybox.render(gl);
    table.draw(gl);
    lamp.draw(gl);

    heli.draw(gl);
    pinboard.draw(gl);
    lampListener();
    lightListener(gl);
    lampMotion(); //Lamp motion listener
    //updateSunPosition();
    heliMotion(); //helicopter motion listener

  }



  /****
  Heli toggle, changes toggleFloat variable accordingly to trigger heliMotion()
  */

  public void heliToggle(){
    aniStart = getSeconds();
    if(!toggleFloat){toggleFloat=true;}
    else{toggleFloat=false;}
  }

  private void lampListener(){
    if(roomLight){lamp.lightOn();}else{lamp.lightOff();}
  }

  public void lightToggle(){
    if(roomLight){roomLight=false;}else if(!roomLight){roomLight=true;};
  }

  private void lightListener(GL3 gl){
    if(roomLight){
      Material material = light.getMaterial();
      material.setAmbient(0.5f, 0.5f, 0.5f);
      material.setDiffuse(0.8f, 0.8f, 0.8f);
      material.setSpecular(0.8f,0.8f,0.8f);
      material.setSpecular(0.8f, 0.8f, 0.8f);
      light.setMaterial(material);
      light.setDim(new Vec3(1.0f,1.0f,1.0f));
      light.render(gl);
    }else{
      Material material = light.getMaterial();
      material.setAmbient(0.1f,0.1f,0.1f);
      material.setDiffuse(0.2f,0.2f,0.2f);
      material.setSpecular(0.1f,0.1f,0.1f);
      light.setMaterial(material);
      light.setDim(new Vec3(0.2f,0.2f,0.2f));
    }
  }
  //HeliLand, initiates landing of helicopter
  private void heliLand(){
    heli.heliLand();
  }


  //Listener for helicopter motion, called in render looks at toggleFloat variable,
  //Automatically lands when toggled off
  private void heliMotion() {
    //aniStart = getSeconds();
    if(toggleFloat){heli.heliFloat(heliHeight, aniStart);}else{heli.heliLand();}
  }


  //Retracts lamp
  public void lampRetract(){
    lamp.setInMotion();
    lampRetracting = true;
  }


  //Creates Vec3 of 3 random floats to pass to the lamp position setter
  public void lampRandom(){
    Random random = new Random();
    rndV = new Vec3(
      (-180f + random.nextFloat() * (180f - (-180f))),
      (-65f + random.nextFloat() * (15f - (-65f))),
      (-90f + random.nextFloat() * (0 - (-90f))));
    lamp.setInMotion();
    lampRandomizing=true;
  }

  //set lamp into default position
  public void lampDefault(){
    lamp.setInMotion();
    lampDefaulting=true;
  }


  //Listener for lamp motion, priority in order is retract > default > random
  private void lampMotion(){
    if (lampRetracting){
      if(lamp.getIfMotion()){
        lamp.retractLamp();
      }else{
        lampRetracting=false;
      }
    }
    else if (lampDefaulting){
      if(lamp.getIfMotion()){
        lamp.defaultLamp();
      }else{
        lampDefaulting=false;
      }
    }
    else if (lampRandomizing){
      if(lamp.getIfMotion()){
        lamp.randomLamp(rndV);
      }else{
        lampRandomizing=false;
      }
    }
  }

  public void lampToggle(){
    if(!lampLight){lampLight=true;}else{lampLight=false;}
  }

  //changes helicopter height, minimum height of 1.2f, max of 4.2f
  public void setHeliHeight(float h){
    this.heliHeight = h/100*3.0f+1.2f;
  }

  public int getTableHeight(){
    return (int)Math.round(this.table.getTableHeight());
  }

  private double startTime;

  private double getSeconds(){
    return System.currentTimeMillis()/1000.0;
  }


}
