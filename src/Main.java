import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.nio.file.*;

public class Main {
    private File selectedFile;
    private JButton JBSelectE;
    private JPasswordField JfSKE;
    private JButton encryptButton;
    private JButton JBSelectD;
    private JPasswordField JfSKD;
    private JButton decryptButton;
    private JPanel canvas;
    private JTextField JfEkstensi;
    private JLabel imageLogo;
    private JButton aboutButton;
    private JButton licenseButton;
    private JButton termsButton;
    private JCheckBox showCheckBox;
    private JCheckBox showCheckBox1;
    private JLabel browseD;
    private JLabel browseE;
    private JLabel SKE;
    private JLabel SKD;
    private static final String LICENSE_PANEL = "License";
    private static final String ABOUT_PANEL = "About";
    private static final String TERMS_PANEL = "Terms";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pangoline Engine 0.7");
        frame.setContentPane(new Main().canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // Ubah path file gambar sesuai dengan lokasi gambar
        String mainIcon = "Assets/pangolin.png";
        String mainBrowse = "Assets/folder.png";
        String mainKey = "Assets/key.png";

        ImageIcon originalIcon = new ImageIcon(mainIcon);
        ImageIcon originalIcon1 = new ImageIcon(mainBrowse);
        ImageIcon originalIcon2 = new ImageIcon(mainKey);

        Image originalImage = originalIcon.getImage();
        Image originalImage1 = originalIcon1.getImage();
        Image originalImage2 = originalIcon2.getImage();

        // Mengubah ukuran gambar ke ukuran yang diinginkan
        Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image scaledImage1 = originalImage1.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Image scaledImage2 = originalImage2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        // Menginisialisasi ImageIcon dengan gambar yang telah disediakan
        imageLogo = new JLabel(new ImageIcon(scaledImage));
        browseE = new JLabel(new ImageIcon(scaledImage1));
        browseD = new JLabel(new ImageIcon(scaledImage1));
        SKE = new JLabel(new ImageIcon(scaledImage2));
        SKD = new JLabel(new ImageIcon(scaledImage2));
    }
    public Main() {
        JBSelectE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    JBSelectE.setText(selectedFile.getName());
                }
            }
        });

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String secretKey = new String(JfSKE.getPassword());
                String extension = JfEkstensi.getText();
                // Memastikan file terpilih tidak null
                if (selectedFile != null) {
                    try {
                        encryptFile(selectedFile, extension, secretKey); // Proses encrypt menggunakan file yang diilih
                    } catch (Exception ex) {
                        ex.printStackTrace(); //Kondisi untuk masalah permisson, bisa berupa file tidak ada di path, dll
                        JOptionPane.showMessageDialog(null, "Failed to encrypt the file. Check file permissions or try again.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No file selected.");
                }
            }
        });

        JBSelectD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    JBSelectD.setText(selectedFile.getName());
                }
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String secretKey = new String(JfSKD.getPassword());
                // Memastikan file terpilih tidak null
                if (selectedFile != null) {
                    try {
                        decryptFile(selectedFile, secretKey); // Menggunakan file terpilih
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to decrypt the file. Check file permissions or try again.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No file selected.");
                }
            }
        });

        licenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String license = "<html><body><p><strong><font size='5'>GNU General Public License (GPL)</font></strong><br>" +
                        "This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License <br>" +
                        "as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.<br>" +
                        "This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty<br>" +
                        "of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.<br>" +
                        "You should have received a copy of the GNU General Public License along with this program. If not, see <a href='https://www.gnu.org/licenses/gpl-3.0.en.html'>GNU GPL License</a>.</p></body></html>";

                JOptionPane.showMessageDialog(null, license, "License", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "<html>" +
                        "<body>" +
                        "<p>" +
                        "<strong><font size='5'>Pangoline</font></strong><br>" +
                        "Program ini menggunakan algoritma enkripsi dan dekripsi Advanced Encryption Standard (AES) dengan mode ECB (Electronic Codebook) dan padding PKCS5.<br>" +
                        "Proses enkripsi dimulai dengan pengambilan kunci rahasia dari pengguna. Kunci ini diubah menjadi kunci 128-bit menggunakan fungsi hashing SHA-1.<br>" +
                        "Proses enkripsi kemudian dilakukan pada file yang dipilih, menghasilkan file terenkripsi dengan ekstensi yang telah ditentukan.<br>" +
                        "Saat dekripsi, kunci rahasia yang sama digunakan untuk mengembalikan file ke keadaan aslinya sebelum dienkripsi." +
                        "</p>" +
                        "</body>" +
                        "</html>";

                JOptionPane.showMessageDialog(null, message, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        termsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String terms = "<html><body><p><strong><font size='5'>Terms and Conditions</font></strong><br>" +
                        "By using this software, you agree to the following terms and conditions: <br>" +
                        "1. The software is provided 'as is', without warranty of any kind, express or implied, including but not limited to the warranties<br>" +
                        "   of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be<br>" +
                        "   liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in<br>" +
                        "   connection with the software or the use or other dealings in the software.<br>" +
                        "2. Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions<br>" +
                        "   are met:<br>" +
                        "   a. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer." +
                "</p></body></html>";

                JOptionPane.showMessageDialog(null, terms, "Terms and Conditions", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        showCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showCheckBox.isSelected()) {
                    JfSKE.setEchoChar((char) 0); // Menampilkan karakter di JPasswordField
                } else {
                    JfSKE.setEchoChar('*'); // Menyembunyikan karakter di JPasswordField
                }
            }
        });

        // Menambahkan ActionListener untuk CheckBox kedua
        showCheckBox1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showCheckBox1.isSelected()) {
                    JfSKD.setEchoChar((char) 0); // Menampilkan karakter di JPasswordField
                } else {
                    JfSKD.setEchoChar('*'); // Menyembunyikan karakter di JPasswordField
                }
            }
        });
    }
    public static void encryptFile(File fileOrFolderPath, String extension, String secretKey) throws IOException {
        if (fileOrFolderPath.exists()) { // Cek apakah file atau folder ada
            if (fileOrFolderPath.isFile()) { // Jika yang dipilih adalah file
                encrypt(fileOrFolderPath, extension, secretKey);
            } else { // Jika yang dipilih adalah folder
                File[] files = fileOrFolderPath.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (!file.getName().equals("contoh.pdf")) { // pengecualian file yang akan dienkrip di dalam folder
                            encrypt(file, extension, secretKey);
                        }
                    }
                }
            }
        } else { //kalo file ga ada
            throw new IOException("File or folder does not exist.");
        }
    }

    public static void encrypt(File file, String extension, String secretKey) {
        try {
            // Konversi secretKey ke array byte dengan encoding UTF-8
            byte[] key = secretKey.getBytes("UTF-8");
            // Hashing menggunakan SHA-1 dan mengonversi menjadi 128-bit key (16 byte)
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            // Inisialisasi objek Cipher untuk enkripsi dengan mode AES/ECB/PKCS5Padding
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // Baca seluruh byte dari file yang akan dienkripsi
            byte[] inputBytes = Files.readAllBytes(file.toPath());
            // Proses enkripsi pada inputBytes dan simpan hasilnya di outputBytes
            byte[] outputBytes;

            // Proses terakhir enkripsi dan simpan hasilnya di outputBytes
            outputBytes = cipher.doFinal(inputBytes);

            /* Buat path file untuk menyimpan hasil enkripsi dengan menambahkan ekstensi
            * yang telah ditentukan oleh user
             */
            String encryptedFilePath = file.getAbsolutePath() + "." + extension;

            // Menimpa (overwrite) file yang ada dengan file hasil enkripsi
            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath() + "." + extension);
            outputStream.write(outputBytes);
            // Tutup output stream setelah selesai menyimpan
            outputStream.close();

            // Hapus file asli setelah proses enkripsi
            Files.delete(file.toPath());
            JOptionPane.showMessageDialog(null, "File has been encrypted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid Secret Key");
        }
    }

    public static void decrypt(File fileOrFolderPath, String secretKey) throws IOException, InvalidKeyException {
        if (fileOrFolderPath.exists()) {
            if (fileOrFolderPath.isFile()) {
                // Jika yang dipilih adalah file
                try {
                    decryptFile(fileOrFolderPath, secretKey);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Jika yang dipilih adalah folder
                decryptFolder(fileOrFolderPath, secretKey);
            }
        } else {
            throw new IOException("File or folder does not exist.");
        }
    }

    public static void decryptFolder(File folder, String secretKey) throws InvalidKeyException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    decryptFolder(file, secretKey); // Proses rekursif jika bertemu subfolder
                } else {
                    decryptFile(file, secretKey);
                }
            }
        }
    }

    public static void decryptFile(File file, String secretKey) throws InvalidKeyException {
        try {
            byte[] key = secretKey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] inputBytes = Files.readAllBytes(file.toPath());
            byte[] outputBytes = cipher.doFinal(inputBytes);

            String decryptedFilePath = file.getAbsolutePath().replace("." + getExtension(file), ""); // Remove the extension
            FileOutputStream outputStream = new FileOutputStream(decryptedFilePath);
            outputStream.write(outputBytes);
            outputStream.close();

            // Hapus file terenkripsi setelah proses dekripsi
            Files.delete(file.toPath());
            JOptionPane.showMessageDialog(null, "File has been decrypted successfully.");
        } catch (AccessDeniedException ex) {
            JOptionPane.showMessageDialog(null, "Access Denied: You don't have permission to access this file or directory.");
        } catch (InvalidKeyException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid Secret Key");
        }
    }

    public static String getExtension(File file) {
        String extension = "";
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = fileName.substring(dotIndex + 1);
        }
        return extension;
    }
}

