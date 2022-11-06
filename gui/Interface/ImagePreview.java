package Interface;

import java.awt.Image;

import javax.swing.ImageIcon;

// Função criada para dimensionar imagens a um tamanho compatível com aquele da janela "File Downloader", de dimensões 600 x 600 px.

public class ImagePreview {
    public static ImageIcon fitImage(byte[] data, int size) {
        ImageIcon image = new ImageIcon(data);
        int height = image.getIconHeight();
        int width = image.getIconWidth();
        int max = (height >= width) ? height : width;
        double mult = size / (double) max;
        width *= mult;
        height *= mult;
        Image preview = image.getImage();
        preview = preview.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(preview);
    }
}
