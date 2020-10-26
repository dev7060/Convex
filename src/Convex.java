/** 
 * github.com/dev7060
 **/
package devProductions;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import java.applet.*;
import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Convex extends Applet implements KeyListener {

    String fps = ""; //frames per second, for later
    String msg = " $ ";
    String events = " > ";
    String modelName = "";
    //diff for diff ops
    DebugW obj;
    private TransformGroup viewtrans = null;
    private TransformGroup tgobj = null;
    private SimpleUniverse universe = null;
    private Canvas3D canvas = null;

    private Transform3D t3d = null;
    private final Transform3D t3dstep = new Transform3D();
    private final Matrix4d matrix = new Matrix4d();

    public Convex() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Convex.class.getName()).log(Level.SEVERE, null, ex);
        }

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);

        universe = new SimpleUniverse(canvas);
        BranchGroup scene = createSceneGraph();
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.getViewer().getView().setBackClipDistance(500.0);
        universe.addBranchGraph(scene);

    }

    //intf overriding
    @Override
    public void keyTyped(KeyEvent e) {

        if (e.getKeyChar() == 'd') {
            t3dstep.set(new Vector3d(10.0, 0.0, 0.0));
            tgobj.getTransform(t3d);
            t3d.mul(t3dstep);
            tgobj.setTransform(t3d);
            ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + "\n" + events + " Keypress d");
        }

        if (e.getKeyChar() == 's') {

            t3dstep.rotY(Math.PI / 32);
            tgobj.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tgobj.setTransform(t3d);
            ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + "\n" + events + " Keypress s");

        }

        if (e.getKeyChar() == 'f') {

            t3dstep.rotY(-Math.PI / 32);
            tgobj.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tgobj.setTransform(t3d);
            ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + "\n " + events + " Keypress f");

        }

        if (e.getKeyChar() == 'e') {

            t3dstep.rotZ(Math.PI / 32);
            tgobj.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tgobj.setTransform(t3d);
            ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + "\n " + events + " Keypress e");

        }

        if (e.getKeyChar() == 'c') {

            t3dstep.rotZ(-Math.PI / 32);
            tgobj.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tgobj.setTransform(t3d);
            ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + "\n " + events + " Keypress c");
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    private BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);
        viewtrans = universe.getViewingPlatform().getViewPlatformTransform();
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
        keyNavBeh.setSchedulingBounds(bounds);
        PlatformGeometry platformGeom = new PlatformGeometry();
        platformGeom.addChild(keyNavBeh);
        universe.getViewingPlatform().setPlatformGeometry(platformGeom);
        objRoot.addChild(modelInit());
        return objRoot;
    }

    private BranchGroup modelInit() {

        BranchGroup objRoot = new BranchGroup();

        tgobj = new TransformGroup();
        t3d = new Transform3D();

        tgobj.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        t3d.setTranslation(new Vector3d(-2.0, 0.0, -15.0));
        t3d.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, 1.57f));
        t3d.setScale(0.1);

        tgobj.setTransform(t3d);

        ObjectFile loader = new ObjectFile();
        Scene s = null;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("obj", "obj");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        String modelNameTemp = "";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            modelNameTemp = chooser.getSelectedFile().getAbsolutePath();
        } else {
            //cancel opn by user
            System.out.println("No file!");
            System.exit(0);
        }
        File file = new java.io.File(modelNameTemp);
        try {
            s = loader.load(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Convex.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
            System.exit(1);
        } catch (FileNotFoundException | IncorrectFormatException | ParsingErrorException ex) {
            Logger.getLogger(Convex.class.getName()).log(Level.SEVERE, null, ex);
        }

        tgobj.addChild(s.getSceneGroup());

        objRoot.addChild(tgobj);

        objRoot.addChild(createLight());
        objRoot.addChild(createLight());

        objRoot.compile();

        return objRoot;

    }

    private Light createLight() {
        DirectionalLight light = new DirectionalLight(true, new Color3f(1.0f, 1.0f, 1.0f),
                new Vector3f(-0.3f, 0.2f, -1.0f));

        light.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

        return light;
    }

    public void cliStuff() {
        obj = new DebugW();
        ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + "\n" + msg + " Frame load OK\n");
        String eventsFrom = "@deV-Workstation"; //for debugging purpose; mainframe's pid too
        String icpth = "lens.png";
        ((JTextArea) obj.fun()).setText(msg + "Link object OK\n");
        ImageIcon img = new ImageIcon(icpth);
        obj.setIconImage(img.getImage());
        Font font = new Font("Corbel", Font.PLAIN, 12);
        ((JTextArea) obj.fun()).setEditable(false);
        ((JTextArea) obj.fun()).setFont(font);
        ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + msg + "Processors: " + Runtime.getRuntime().availableProcessors());
        ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + "\n" + msg + "Begin OK\n");
        ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + msg + "Availabe memory: " + Runtime.getRuntime().freeMemory());
        ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + "\n" + msg + "In use memory: " + Runtime.getRuntime().totalMemory()); //analysis
        obj.setTitle(eventsFrom);
        obj.setVisible(true);
        ((JTextArea) obj.fun()).setBackground(Color.BLACK);

        ((JTextArea) obj.fun()).setForeground(Color.GREEN);

        ((JTextArea) obj.fun()).setText(((JTextArea) obj.fun()).getText() + " $ " + msg + "Initialise OK\n");
    }

    public void initSet() {
        add("Center", canvas);
        canvas.addKeyListener(this);
    }

    public static void main(String... args) {
        //may need to use Xmx flag if running with cli
        java.awt.EventQueue.invokeLater(() -> {
            Convex applet = new Convex();
            applet.setLayout(new BorderLayout());
            applet.initSet();
//				  to get the model name, not again; dm to be used
//                JFileChooser chooser = new JFileChooser();
//                FileNameExtensionFilter filter = new FileNameExtensionFilter("CvX", "cvx");
//                chooser.setFileFilter(filter);
//                int returnVal = chooser.showOpenDialog(this);
//                if (returnVal == JFileChooser.APPROVE_OPTION) {
//                    String modelName = chooser.getSelectedFile().getAbsolutePath();
//                }catch (Exception e) {
//                        System.out.println("error");
//                    }
            String modelNameTemp = "untitledModel1.cvx"; //temp
            String title = "Convex Game Engine ";
            String version = "(α 1.0.101)";
            String tempFPS = "";
            Integer width = 1200; //frame
            Integer height = 720; //frame
            Frame frame = new MainFrame(applet, width, height);
            ImageIcon img = new ImageIcon("lens.png");

            frame.setIconImage(img.getImage());
            frame.setTitle(modelNameTemp + " - " + title + version + tempFPS);
            applet.cliStuff();
        });
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            System.out.println("GC [Convex.class]");
        } finally {
            super.finalize();
        }
    }
}
