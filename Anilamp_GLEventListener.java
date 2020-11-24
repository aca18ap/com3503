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
    floor.dispose(gl);
    light.dispose(gl);
    //table.dispose(gl);
  }


  /**** SCENE ****/

  /* declarations here*/

  private Light light;
  private Model floor, window, wall, tableTop, tableLeg;
  private SGNode tableRoot;
  private Table table;
  private Lamp lamp;

  public void initialise(GL3 gl){
    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/wall.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/floor.jpg");
    light = new Light(gl);
    light.setCamera(camera);
    floor = initFloor(gl, textureId1);
    wall = initWall(gl, textureId0);

    table = new Table(gl, camera, light);
    lamp = new Lamp(gl, camera, light);

  }

  public void render(GL3 gl){
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(2,2,2);
    light.render(gl);


    floor.setModelMatrix(getMfloor());
    floor.render(gl);
    wall.setModelMatrix(getMnorthWall());
    wall.render(gl);
    wall.setModelMatrix(getMwestWall());
    wall.render(gl);

    table.draw(gl);
    lamp.draw(gl);
    lamp.updateLowerArmX();
    lamp.updateLowerArmY();
    //lamp.updateUpperArmX();
  }


  // Initialises plane model for floor and walls

  public Model initFloor(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

  public Model initWall(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

  public Model initWindow(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Window.vertices.clone(), Window.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.0f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model window = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return window;
  }

  public Model initTableLeg(GL3 gl, int[] t){
      Mesh m = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
      Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
      Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
      Model leg = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
      return leg;
  }

  // Matrix model for floor
  private Mat4 getMfloor(){
    float size = 4f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMnorthWall(){
    float size = 4f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMwestWall(){
    float size = 4f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMwindow(){
    float size = 4f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }



  private double startTime;

  private double getSeconds(){
    return System.currentTimeMillis()/1000.0;
  }


}
