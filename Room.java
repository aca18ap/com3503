import gmaths.*;
import com.jogamp.opengl.*;


public final class Room {

  private Model floor, wall, window;
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
    window = initWindow(gl, textureId0);

    floor.setModelMatrix(getMfloor());
    //floor.render(gl);
    wall.setModelMatrix(getMnorthWall());
    //wall.render(gl);
    window.setModelMatrix(getMwindow());
    //window.render(gl);

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
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

  private Model initWindow(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Window.vertices.clone(), Window.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model window = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
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


  private Mat4 getMwindow(){
    float size = 4f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  public void render(GL3 gl){
    floor.render(gl);
    window.render(gl);
    wall.render(gl);
  }
}
