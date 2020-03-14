package ZIPPER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Zipper extends JFrame
{
    public Zipper()
    {
        
        this.setTitle("Zipper");
        this.setBounds(275, 300, 250, 250);
        this.setJMenuBar(pasekMenu);
        
        JMenu menuPlik = pasekMenu.add(new JMenu("Plik"));
        
        
        Action akcjaDodawania = new Akcja("Dodaj", "Dodaj nowy wpis do archiwum.", "ctrl D", new ImageIcon("add-stock-icon.png"));
        Action akcjaUsuwania = new Akcja("Usuń", "Usuń zaznaczone wpisy z archiwum.", "ctrl U", new ImageIcon("delete-stock-icon.png"));
        Action akcjaZipowania = new Akcja("Zip", "Zipuj plik","ctrl Z", new ImageIcon("zip-stock-icon.png"));
        
        JMenuItem menuOtworz = menuPlik.add(akcjaDodawania);
        JMenuItem menuUsun = menuPlik.add(akcjaUsuwania);
        JMenuItem menuZip = menuPlik.add(akcjaZipowania);
        
        
        bDodaj = new JButton(akcjaDodawania);
        bUsun = new JButton(akcjaUsuwania);
        bZip = new JButton(akcjaZipowania);
        
        lista.setBorder(BorderFactory.createTitledBorder("Lista"));
        
        GroupLayout layout = new GroupLayout(this.getContentPane());
        
        
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addComponent(scroller, 100, 150, Short.MAX_VALUE)
                .addContainerGap(0, Short.MAX_VALUE)
                .addGroup(
                layout.createParallelGroup().addComponent(bDodaj).addComponent(bUsun).addComponent(bZip)
                
                
                )
            );
        layout.setVerticalGroup(
                layout.createParallelGroup()
                .addComponent(scroller, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup().addComponent(bDodaj).addComponent(bUsun).addGap(5,40, Short.MAX_VALUE).addComponent(bZip))
        
        
        
        );
        
       
       this.getContentPane().setLayout(layout);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
       
      
       
       this.pack();
    }
    
    
    
    private DefaultListModel modelListy = new DefaultListModel()
    {
        @Override
        public void addElement(Object obj) 
        {
            
            lista.add(obj);
            super.addElement(((File)obj).getName());
        
        }
        public Object get(int index)
        {
            return lista.get(index);
        }
        
        public Object remove(int index)
        {
            lista.remove(index);
            return super.remove(index);
        }
        ArrayList lista = new ArrayList();
    };
    private JOptionPane frame = new JOptionPane();
    private ImageIcon img = new ImageIcon("Main-icon.png");
    private JList lista = new JList(modelListy);
    private JButton bDodaj;
    private JButton bUsun;
    private JButton bZip;
    JScrollPane scroller = new JScrollPane(lista);
    private JMenuBar pasekMenu = new JMenuBar();
    private JFileChooser wybieracz = new JFileChooser();
    
    
    public static void main(String[] args)
    {

        new Zipper().setVisible(true);
    }
    
    private class Akcja extends AbstractAction
    {
        public Akcja(String nazwa, String opis, String klawiaturowySkrot)
        {
            this.putValue(Action.NAME, nazwa);
            this.putValue(Action.SHORT_DESCRIPTION, opis);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(klawiaturowySkrot));
            
        }
        
        public Akcja(String nazwa, String opis, String klawiaturowySkrot, Icon ikona)
        {
            this(nazwa, opis, klawiaturowySkrot);
            this.putValue(Action.SMALL_ICON, ikona);
            
        }
        

        @Override
        public void actionPerformed(ActionEvent e) 
        {
          if  (e.getActionCommand().equals("Dodaj"))
                dodajWpisyDoArchiwum();
          else if (e.getActionCommand().equals("Usuń"))
                usuwanieWpisowZListy();
          else if (e.getActionCommand().equals("Zip"))
                stworzArchiwumZip();
                  
               
        }
        private void dodajWpisyDoArchiwum()
        {
            wybieracz.setCurrentDirectory(new File(System.getProperty("user.dir")));
            wybieracz.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            wybieracz.setMultiSelectionEnabled(enabled);
            int tmp = wybieracz.showDialog(rootPane, "Dodaj do archiwum");
            
            if (tmp == JFileChooser.APPROVE_OPTION)
            {
               File[] sciezki =  wybieracz.getSelectedFiles();
               
               for (int i = 0; i < sciezki.length; i++)
                   if(!czyWpisSiePowtarza(sciezki[i].getPath()))
                   modelListy.addElement(sciezki[i]);
                   
                       
              
            }
        }
        
        private boolean czyWpisSiePowtarza(String testowanyWpis)
                
                {
                    for (int i = 0; i < modelListy.getSize(); i++)
                        if( ((File)modelListy.get(i)).getPath().equals(testowanyWpis))
                            return true;
                    
                    
                    return false;
                }
        private void usuwanieWpisowZListy()
        {
            int opt = JOptionPane.showConfirmDialog(null, "Czy jesteś pewny, ze chcesz usunąć plik z listy?", "Usuwanie pliku z listy", JOptionPane.YES_NO_OPTION);
            if (opt == 0)
            {
            int[] tmp = lista.getSelectedIndices();
            
            for(int i = 0; i < tmp.length; i++)
            modelListy.remove(tmp[i]-i);
            }
            
        }
        
        private void stworzArchiwumZip()
        {
            wybieracz.setCurrentDirectory(new File(System.getProperty("user.dir")));
            wybieracz.setSelectedFile(new File(System.getProperty("user.dir")+File.separator+"mojanazwa.zip"));
            int tmp = wybieracz.showDialog(rootPane, "Kompresuj");
            
            if (tmp == JFileChooser.APPROVE_OPTION)
            {
               byte tmpData[] = new byte[BUFFOR];
               try
               {
                ZipOutputStream zOutS = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(wybieracz.getSelectedFile()),BUFFOR));

                for (int i = 0; i < modelListy.getSize(); i++)
                {
                    if (!((File)modelListy.get(i)).isDirectory())
                        zipuj(zOutS, (File)modelListy.get(i), tmpData, ((File)modelListy.get(i)).getPath());
                    else
                    {
                        wypiszSciezki((File)modelListy.get(i));
                                
                        for (int j = 0; j < listaSciezek.size(); j++)
                          zipuj(zOutS, (File)listaSciezek.get(j), tmpData, ((File)modelListy.get(i)).getPath());
                        
                        listaSciezek.removeAll(listaSciezek);
                    }
                        
                }

                zOutS.close();
               }
               catch(IOException e)
               {
                   System.out.println(e.getMessage());
               }
            }
        }
        private void zipuj(ZipOutputStream zOutS, File sciezkaPliku, byte[] tmpData, String sciezkaBazowa) throws IOException
        {
            BufferedInputStream inS = new BufferedInputStream(new FileInputStream(sciezkaPliku), BUFFOR);

            zOutS.putNextEntry(new ZipEntry(sciezkaPliku.getPath().substring(sciezkaBazowa.lastIndexOf(File.separator)+1)));
            
            int counter;
            while ((counter = inS.read(tmpData, 0, BUFFOR)) != -1)
                zOutS.write(tmpData, 0, counter);


            zOutS.closeEntry();

            inS.close();            
        }
        public static final int BUFFOR = 1024;
       
        private void wypiszSciezki(File nazwaSciezki)
        {
           String[] nazwyPlikowIKatalogow = nazwaSciezki.list();

           for (int i = 0; i < nazwyPlikowIKatalogow.length; i++)
           {
               File p = new File(nazwaSciezki.getPath(), nazwyPlikowIKatalogow[i]);

               if (p.isFile())
                   listaSciezek.add(p);

               if (p.isDirectory())
                   wypiszSciezki(new File(p.getPath()));

           }
        }
        
        ArrayList listaSciezek = new ArrayList();
    }
    
    
    
}
