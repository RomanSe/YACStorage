package com.rs.common.messages;

import com.rs.common.Context;
import com.rs.common.model.FileDescriptor;

public class GetFileCommand extends Command {

    private static final long serialVersionUID = 7797172053116729390L;

    private FileDescriptor fileDescriptor;

    @Override
    public Response process(Context context) {
        return null;
    }
}
