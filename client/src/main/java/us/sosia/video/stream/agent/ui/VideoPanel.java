package us.sosia.video.stream.agent.ui;

import net.coobird.thumbnailator.makers.ScaledThumbnailMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Видео-панель
 */
public class VideoPanel extends JPanel {

    private static final long serialVersionUID = -7292145875292244144L;

    protected BufferedImage image;
    protected final ExecutorService worker = Executors.newSingleThreadExecutor();
    protected final ScaledThumbnailMaker scaleUPMaker = new ScaledThumbnailMaker(2);
    protected volatile Boolean visable = true;

    /**
     * Прорисовка изображения
     *
     * @param g
     */
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (image == null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setBackground(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            int cx = (getWidth() - 70) / 2;
            int cy = (getHeight() - 40) / 2;

            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRoundRect(cx, cy, 70, 40, 10, 10);
            g2.setColor(Color.WHITE);
            g2.fillOval(cx + 5, cy + 5, 30, 30);
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval(cx + 10, cy + 10, 20, 20);
            g2.setColor(Color.WHITE);
            g2.fillOval(cx + 12, cy + 12, 16, 16);
            g2.fillRoundRect(cx + 50, cy + 5, 15, 10, 5, 5);
            g2.fillRect(cx + 63, cy + 25, 7, 2);
            g2.fillRect(cx + 63, cy + 28, 7, 2);
            g2.fillRect(cx + 63, cy + 31, 7, 2);

            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(0, 0, getWidth(), getHeight());
            g2.drawLine(0, getHeight(), getWidth(), 0);

            String str = image == null ? "Connecting To Server" : "No Image";
            FontMetrics metrics = g2.getFontMetrics(getFont());
            int w = metrics.stringWidth(str);
            int h = metrics.getHeight();

            g2.setColor(Color.WHITE);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.drawString(str, (getWidth() - w) / 2, cy - h);
        } else {
            int width = image.getWidth();
            int height = image.getHeight();
            g2.clearRect(0, 0, width, height);
            g2.drawImage(image, 0, 0, width, height, null);
            setSize(width, height);
        }
    }

    /**
     * обновление изображения
     *
     * @param update
     */
    public void updateImage(final BufferedImage update) {
        synchronized (visable) {
            if (visable)
                worker.execute(() -> {
                    image = update;
                    repaint();
                });
        }
    }

    /**
     * установка видимости прорисовки
     */
    public void setVisable(Boolean visable) {
        synchronized (visable) {
            this.visable = visable;
        }
    }

    /**
     * включена ли прорисовка видео
     *
     * @return да\нет
     */
    public Boolean getVisable() {
        synchronized (visable) {
            return visable;
        }
    }
}
