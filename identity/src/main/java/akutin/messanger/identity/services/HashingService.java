package akutin.messanger.identity.services;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashingService {
    private HashingService() {}

    public static String hash(String stringToHashing) {
        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher()
                .putString(stringToHashing, Charsets.UTF_8)
                .hash();

        return hc.toString();
    }
}
