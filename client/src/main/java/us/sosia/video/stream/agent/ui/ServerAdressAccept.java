package us.sosia.video.stream.agent.ui;

import java.io.IOException;

/**
 * Created by idony on 24.12.16.
 */
public interface ServerAdressAccept  {
    public void init() throws IOException;
    public String run(String key);
}
