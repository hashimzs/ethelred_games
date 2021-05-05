package org.ethelred.games.server;

import javax.inject.Inject;
import java.util.Map;

public class ProfileLoader
{
    private final Map<String, Profile> availableProfiles;

    @Inject
    public ProfileLoader(Map<String, Profile> availableProfiles)
    {
        this.availableProfiles = availableProfiles;
    }

    public Profile load(String name)
    {
        var profile = availableProfiles.get(name);
        if (profile == null)
        {
            throw new IllegalArgumentException("No profile with name " + name);
        }
        return profile;
    }
}
