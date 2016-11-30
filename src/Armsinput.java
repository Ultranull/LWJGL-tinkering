

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by usr on 11/20/2016.
 *
 */
public class Armsinput extends JFrame{

    private JSpinner arm1x;
    private JSpinner arm1y;
    private JSpinner arm1z;
    private JSpinner arm2x;
    private JSpinner arm2y;
    private JSpinner arm2z;
    private JSpinner arm3x;
    private JSpinner arm3y;
    private JSpinner arm3z;
    private JButton applyButton;
    private JButton exitButton;
    private JPanel mainpanel;
    private JButton randomButton;
    private JCheckBox arm1active;
    private JCheckBox arm2active;
    private JCheckBox arm3active;

    public boolean isApplied=false;
    public boolean beRandom=false;
    private boolean isExited=false;
    public boolean a1a=false,a2a=false,a3a=true;

    public double[] data;
    public Armsinput(){
        arm1x.setModel(new SpinnerNumberModel(0,-10,10,1));
        arm1y.setModel(new SpinnerNumberModel(0,-10,10,1));
        arm1z.setModel(new SpinnerNumberModel(0,-10,10,1));
        arm2x.setModel(new SpinnerNumberModel(0,-10,10,1));
        arm2y.setModel(new SpinnerNumberModel(0,-10,10,1));
        arm2z.setModel(new SpinnerNumberModel(0,-10,10,1));
        arm3x.setModel(new SpinnerNumberModel(0,-10,10,1));
        arm3y.setModel(new SpinnerNumberModel(0,-10,10,1));
        arm3z.setModel(new SpinnerNumberModel(0,-10,10,1));
        init();


    }
    public Armsinput(float[] inits){
        arm1x.setModel(new SpinnerNumberModel(inits[0],-0xff,-0xff,1));
        arm1y.setModel(new SpinnerNumberModel(inits[1],-0xff,0xff,1));
        arm1z.setModel(new SpinnerNumberModel(inits[2],-0xff,0xff,1));
        arm2x.setModel(new SpinnerNumberModel(inits[3],-0xff,0xff,1));
        arm2y.setModel(new SpinnerNumberModel(inits[4],-0xff,0xff,1));
        arm2z.setModel(new SpinnerNumberModel(inits[5],-0xff,0xff,1));
        arm3x.setModel(new SpinnerNumberModel(inits[6],-0xff,0xff,1));
        arm3y.setModel(new SpinnerNumberModel(inits[7],-0xff,0xff,1));
        arm3z.setModel(new SpinnerNumberModel(inits[8],-0xff,0xff,1));
        init();

    }
    private void init(){
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(679, 257);
        setContentPane(mainpanel);
        data=new double[]{(double)arm1x.getValue(),(double)arm1y.getValue(),(double)arm1z.getValue()
                ,(double)arm2x.getValue(),(double)arm2y.getValue(),(double)arm2z.getValue()
                ,(double)arm3x.getValue(),(double)arm3y.getValue(),(double)arm3z.getValue()};
        applyButton.addActionListener(e -> {
            if(e.getActionCommand().equals("apply")){
                isApplied=!isApplied;
                if(isApplied){
                    data=new double[]{(double)arm1x.getValue(),(double)arm1y.getValue(),(double)arm1z.getValue()
                            ,(double)arm2x.getValue(),(double)arm2y.getValue(),(double)arm2z.getValue()
                            ,(double)arm3x.getValue(),(double)arm3y.getValue(),(double)arm3z.getValue()};
                }
            }
        });
        exitButton.addActionListener(e -> {
            if(e.getActionCommand().equals("exit")){
                isExited=!isExited;
                if(isExited)
                    System.exit(0);
            }

        });
        randomButton.addActionListener(e -> {
            if(e.getActionCommand().equals("random")){
                beRandom=!beRandom;
            }
        });

        arm1active.addActionListener(e -> a1a=!a1a);
        arm2active.addActionListener(e -> a2a=!a2a);
        arm3active.addActionListener(e -> a3a=!a3a);
    }
public void updateSpinners(){
    arm1x.setValue(data[0]);
    arm1y.setValue(data[1]);
    arm1z.setValue(data[2]);
    arm2x.setValue(data[3]);
    arm2y.setValue(data[4]);
    arm2z.setValue(data[5]);
    arm3x.setValue(data[6]);
    arm3y.setValue(data[7]);
    arm3z.setValue(data[8]);
}
}
