package org.ethelred.games.server;

import dagger.Component;

@Component(modules = {DevProfile.class})
public interface ProfileLoaderFactory
{
    ProfileLoader loader();
}
