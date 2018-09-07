package com.kahramani.p2p.domain.service.transfer;

import com.kahramani.p2p.application.model.request.TransferCreationRequest;
import com.kahramani.p2p.application.model.response.TransferCreationResponse;

public interface TransferCreationService {

    TransferCreationResponse create(String accountReference, TransferCreationRequest transferCreationRequest);
}