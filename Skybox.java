import gmaths.*;
import com.jogamp.opengl.*;


public final class Skybox {

  private Model ground, sky, north, south, west, east;
  private Camera camera;
  private Light light;

  private int worldSize = 64;


  public Skybox(GL3 gl, Camera camera, Light light){

    //this.plane = new Plane(gl, camera, light);
    this.camera = camera;
    this.light = light;
    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/TropicalSunnyDay_ny.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/TropicalSunnyDay_nx.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "./textures/TropicalSunnyDay_pz.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "./textures/TropicalSunnyDay_px.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "./textures/TropicalSunnyDay_nz.jpg");
    int[] textureId5 = TextureLibrary.loadTexture(gl, "./textures/TropicalSunnyDay_py.jpg");




    ground = initGround(gl, textureId0);
    east = initSky(gl, textureId3);
    west = initSky(gl, textureId1);
    north = initSky(gl, textureId2);
    south = initSky(gl, textureId4);
    sky = initSky(gl, textureId5);

    ground.setModelMatrix(getMground());
    sky.setModelMatrix(getMtop());
    east.setModelMatrix(getMeast());
    west.setModelMatrix(getMwest());
    north.setModelMatrix(getMnorth());
    south.setModelMatrix(getMsouth());

  }

  private Model initGround(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

  private Model initSky(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

  private Mat4 getMground(){
    float size = (float)worldSize;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,-size/2,0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMtop(){
    float size = (float)worldSize;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(180), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, size/2, 0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMnorth(){
    float size = (float)worldSize;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,0,-size*0.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMsouth(){
    float size = (float)worldSize;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(180), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,0,size*0.5f), modelMatrix);
    return modelMatrix;
  }


  private Mat4 getMwest(){
    float size = (float)worldSize;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,0,0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMeast(){
    float size = (float)worldSize;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*0.5f,0,0), modelMatrix);
    return modelMatrix;
  }

  public void render(GL3 gl){
    sky.render(gl);
    ground.render(gl);
    north.render(gl);
    south.render(gl);
    east.render(gl);
    west.render(gl);
  }
}
