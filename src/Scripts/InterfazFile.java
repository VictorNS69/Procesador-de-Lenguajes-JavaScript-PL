package Scripts;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.UIManager;

//Interfaz del programa
public class InterfazFile {

	private JFrame frmPracticapdl;
	private JTextField textField;
	private JTextField textField_1;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazFile window = new InterfazFile();
					window.frmPracticapdl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public InterfazFile() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPracticapdl = new JFrame();
		frmPracticapdl.setTitle("PracticaPDL Grupo81");
		frmPracticapdl.setBounds(100, 100, 545, 517);
		frmPracticapdl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPracticapdl.setLocationRelativeTo(null); 
		JLabel lblNewLabel = new JLabel("Archivo a analizar:");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Examinar");
		btnNewButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser filechooser = new JFileChooser();
				filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				filechooser.setAcceptAllFileFilterUsed(false);
				filechooser.addChoosableFileFilter(new FileNameExtensionFilter("*.js", "js"));
				filechooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
				int opcion = filechooser.showOpenDialog(frmPracticapdl);
				if (opcion == JFileChooser.APPROVE_OPTION){
					textField.setText(filechooser.getSelectedFile().getPath());
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Comenzar Analizador");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedReader br = null;
				try{
					FileReader fr = new FileReader(textField.getText());
					br = new BufferedReader(fr);
					//Pasamos el archivo leido
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Debe seleccionar un archivo de texto");
				}
				
				if(!textField_1.getText().isEmpty()){
					AnManager.setPath(textField_1.getText());
					try {
						//Comenzamos a ejecutar el an�lisis
						AnManager.pPrincipal(br);
						JOptionPane.showMessageDialog(null, "An�lisis realizado con �xito");
						frmPracticapdl.dispose();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error al escribir los archivos en el directorio seleccionado");
					}
				}else{
					JOptionPane.showMessageDialog(null, "Debe seleccionar un directorio de salida");
				}
			}
		});
		btnNewButton_1.setForeground(Color.BLACK);
		
		JLabel lblDirectorioDestino = new JLabel("Directorio destino:");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);

		JButton button = new JButton("Examinar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(System.getProperty("user.home")+"/Desktop"); 
			    //chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Elija directorio");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);
			    int opcion = chooser.showOpenDialog(frmPracticapdl);
				if (opcion == JFileChooser.APPROVE_OPTION){
					textField_1.setText(chooser.getSelectedFile().getPath());
				}
			}
		});
		
		JLabel lblSeRealizarEl = new JLabel("Se realizar\u00E1 el an\u00E1lisis del archivo seleccionado");
		lblSeRealizarEl.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblSeGuardarnLos = new JLabel("Se enviar\u00E1n los resultados al directorio seleccionado");
		lblSeGuardarnLos.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JTextPane txtpnImportanteSeCrear = new JTextPane();
		txtpnImportanteSeCrear.setBackground(UIManager.getColor("Menu.background"));
		txtpnImportanteSeCrear.setEditable(false);
		txtpnImportanteSeCrear.setText("Importante: \r\nSe crear\u00E1 una carpeta \"Resultados Grupo81\"  con los archivos resultantes, si estos archivos existen previamente, ser\u00E1n sobreescritos");
		GroupLayout groupLayout = new GroupLayout(frmPracticapdl.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addComponent(btnNewButton, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSeRealizarEl, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(txtpnImportanteSeCrear, GroupLayout.PREFERRED_SIZE, 345, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
							.addComponent(btnNewButton_1))
						.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addComponent(lblDirectorioDestino, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSeGuardarnLos, GroupLayout.PREFERRED_SIZE, 414, GroupLayout.PREFERRED_SIZE)
						.addComponent(button, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(22)
					.addComponent(lblSeRealizarEl, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton)
					.addGap(58)
					.addComponent(lblSeGuardarnLos, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblDirectorioDestino)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(button)
					.addPreferredGap(ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnNewButton_1)
						.addComponent(txtpnImportanteSeCrear, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		frmPracticapdl.getContentPane().setLayout(groupLayout);
	}
}
