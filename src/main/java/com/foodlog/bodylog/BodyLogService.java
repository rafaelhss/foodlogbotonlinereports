package com.foodlog.bodylog;

import com.foodlog.Sender;
import com.foodlog.entity.BodyLog;
import com.foodlog.entity.BodyLogGif;
import com.foodlog.entity.user.User;
import com.github.dragon66.AnimatedGIFWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

/**
 * Created by rafa on 07/09/17.
 */
@Service
public class BodyLogService {

    @Autowired
    BodyLogRepository bodyLogRepository;

    public BodyLogGif getBodyGif(User user) {

        try {
            List<BodyLog> logs = bodyLogRepository.findByUser(user);

            // True for dither. Will use more memory and CPU
            AnimatedGIFWriter writer = new AnimatedGIFWriter(true);
            String fileName = "teste.gif";
            OutputStream os = new FileOutputStream(fileName);
            // Grab the BufferedImage whatever way you can

            // Use -1 for both logical screen width and height to use the first frame dimension
            writer.prepareForWrite(os, -1, -1);


            for(BodyLog bodyLog : logs) {
                InputStream in = new ByteArrayInputStream(bodyLog.getPhoto());
                BufferedImage frame = ImageIO.read(in);
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                                .withLocale( Locale.UK )
                                .withZone( ZoneId.systemDefault() );
                String text = formatter.format(bodyLog.getBodyLogDatetime());

                insertDateToImage(frame, text);

                writer.writeFrame(os, frame,2500);
                // Keep adding frame here

                System.out.println("opa");
            }
            writer.finishWrite(os);



//           String fileName = "/home/rafael/Pictures/teste.gif";

            new Sender("380968235:AAGqnrSERR8ABcw-_avcPN2ES3KH5SeZtNM").sendImage(153350155, fileName);

            return new BodyLogGif(Files.readAllBytes(Paths.get(fileName)));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void insertDateToImage(BufferedImage frame, String text) {
        Graphics2D g2d = frame.createGraphics();

        g2d.setFont(new Font("Arial", Font.PLAIN, 60));
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.setColor(Color.BLACK);
        g2d.setBackground(Color.white);

        //g2d.drawString(text, 0, fm.getAscent());


        g2d.drawString(text, 0, g2d.getFontMetrics().getHeight());

        g2d.dispose();
    }
}
