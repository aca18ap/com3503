import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;


public class Table {

  private float tableWidth = 2f;
  private float tableDepth = 1f;
  private float tableHeight = 1f;
  private float tableLegSize = 0.1f;

  private Model top, leg;
  private SGNode tableRoot;

  private Camera camera;
  private Light light;

  public Table(GL3 gl, Camera camera, Light light){

    this.camera = camera;
    this.light = light;

    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/wall.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/floor.jpg");

    top = initTop(gl, textureId0);
    leg = initLeg(gl, textureId1);


    tableRoot = new NameNode("tableRoot");
    TransformNode tableTopTranslate = new TransformNode("table transform", Mat4Transform.translate(0, tableHeight, 0));

    NameNode tableTopName = new NameNode("tableTop");
      Mat4 m = Mat4Transform.scale(tableWidth, 0.1f, tableDepth);
      m = Mat4.multiply(m, Mat4Transform.translate(0, tableHeight, 0));
      TransformNode tableTransform = new TransformNode("tableTop transform", m);
        ModelNode tableTopShape = new ModelNode("Plane(top)", top);


    NameNode tableLegName = new NameNode("table leg");
      TransformNode tableLegTranslate = new TransformNode("tableleg translate",
                                            Mat4Transform.translate((-tableWidth/2+tableLegSize/2),tableHeight/2,(-tableDepth/2+tableLegSize/2)));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(tableLegSize, tableHeight, tableLegSize));
      m = Mat4.multiply(m, Mat4Transform.translate(0,-tableHeight,0));
      TransformNode tableLegScale = new TransformNode("TopLeft leg scale", m);
        ModelNode tableLegShape = new ModelNode("Cube(0)", leg);

      tableRoot.addChild(tableTopTranslate);
        tableTopTranslate.addChild(tableTopName);
          tableTopName.addChild(tableTransform);
            tableTransform.addChild(tableTopShape);
          tableTopName.addChild(tableLegName);
            tableLegName.addChild(tableLegTranslate);
            tableLegTranslate.addChild(tableLegScale);
              tableLegScale.addChild(tableLegShape);
      tableRoot.update();


  }

  public void draw(GL3 gl){
    tableRoot.draw(gl);
  }



  private Model initLeg(GL3 gl, int[] t){
      Mesh m = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
      Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
      Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
      Model leg = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
      return leg;
  }


  private Model initTop(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

}
