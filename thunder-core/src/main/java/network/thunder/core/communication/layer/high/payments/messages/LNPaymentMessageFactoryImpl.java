package network.thunder.core.communication.layer.high.payments.messages;

import network.thunder.core.communication.layer.MesssageFactoryImpl;
import network.thunder.core.communication.layer.high.Channel;
import network.thunder.core.database.DBHandler;
import org.bitcoinj.crypto.TransactionSignature;

import java.util.ArrayList;
import java.util.List;

public class LNPaymentMessageFactoryImpl extends MesssageFactoryImpl implements LNPaymentMessageFactory {

    public LNPaymentMessageFactoryImpl (DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    DBHandler dbHandler;

    @Override
    public LNPaymentAMessage getMessageA (Channel channel, ChannelUpdate statusTemp) {
        return new LNPaymentAMessage(statusTemp, dbHandler.createRevocationHash(channel));
    }

    @Override
    public LNPaymentBMessage getMessageB (Channel channel) {
        return new LNPaymentBMessage(dbHandler.createRevocationHash(channel));
    }

    @Override
    public LNPaymentCMessage getMessageC (Channel channel, List<TransactionSignature> channelSignatures, List<TransactionSignature> paymentSignatures) {
        List<byte[]> sigList = new ArrayList<>();
        for (TransactionSignature p : paymentSignatures) {
            sigList.add(p.encodeToBitcoin());
        }
        return new LNPaymentCMessage(channelSignatures.get(0).encodeToBitcoin(),
                channelSignatures.get(1).encodeToBitcoin(), sigList);
    }

    @Override
    public LNPaymentDMessage getMessageD (Channel channel) {
        return new LNPaymentDMessage(dbHandler.getOldRevocationHashes(channel));
    }
}
