package com.craftingdead.mod.masterserver.modclient;

import com.craftingdead.network.protocol.IProtocol;
import com.craftingdead.network.protocol.ISession;

public class ModClientSession implements ISession {

  @Override
  public IProtocol<?, ?> getProtocol() {
    return ModClientProtocol.INSTANCE;
  }
}