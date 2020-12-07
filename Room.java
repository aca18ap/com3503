import gmaths.*;
import com.jogamp.opengl.*;


public final class Room {

  private Model floor, wall, window, glass;
  private Camera camera;
  private Light light;


  public Room(GL3 gl, Camera camera, Light light){

    //this.plane = new Plane(gl, camera, light);
    this.camera = camera;
    this.light = light;
    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/wall.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/floor.jpg");

    floor = initFloor(gl, textureId1);
    wall = initWall(gl, textureId0);
    window = initWindowWall(gl, textureId0);
    //glass = initWindowGlass(gl);

    floor.setModelMatrix(getMfloor());
    //floor.render(gl);
    wall.setModelMatrix(getMnorthWall());
    //wall.render(gl);
    window.setModelMatrix(getMwindowWall());
    //window.render(gl);
    //glass.setModelMatrix(getMwindowWall());

  }

  private Model initFloor(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

  private Model initWall(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.2f, 0.2f, 0.2f), new Vec3(0.8f, 0.8f, 0.8f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

  private Model initWindowWall(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Window.vertices.clone(), Window.indicesWall.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.2f, 0.2f, 0.2f), new Vec3(0.8f, 0.8f, 0.8f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model window = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return window;
  }

  private Model initWindowGlass(GL3 gl){
    Mesh m = new Mesh(gl, Window.verticesGlass.clone(), Window.indicesWindow.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), 1.0f);
    //gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    //gl.glEnable( GL.GL_BLEND );  // default is 'back', assuming CCW
    Model window = new Model(gl, camera, light, shader, material, new Mat4(1), m);
    return window;
  }

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


  private Mat4 getMwindowWall(){
    float size = 4f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  //private Mat4 getMwindowGlass(){}

  public void render(GL3 gl){
    floor.render(gl);
    window.render(gl);
    wall.render(gl);
    //glass.render(gl);
  }

  public void dispose(GL3 gl){
    floor.dispose(gl);
    wall.dispose(gl);
    window.dispose(gl);
    //glass.dispose(gl);
  }
}
