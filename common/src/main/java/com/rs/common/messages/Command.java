package com.rs.common.messages;

import com.rs.common.Context;

import java.io.Serializable;

public abstract class Command implements Serializable {
    private static final long serialVersionUID = 4987445173081793295L;

    public abstract Response process(Context context);
}
