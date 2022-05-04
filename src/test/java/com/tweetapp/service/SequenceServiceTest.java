package com.tweetapp.service;

import com.tweetapp.entity.IdSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SequenceServiceTest {

    @InjectMocks
    SequenceServiceImpl sequenceService;

    @Mock
    MongoOperations mongoOperations;

    @Test
    void testGetNextSequence() {
        IdSequence idSequence = Mockito.mock(IdSequence.class);
        when(mongoOperations.findAndModify(Mockito.any(Query.class), Mockito.any(Update.class), Mockito.any(FindAndModifyOptions.class), Mockito.any())).thenReturn(idSequence);
        when(idSequence.getSequence()).thenReturn(1);
        Integer response = sequenceService.getNextSequence("src/test");
        assertEquals(1, response);
    }

}
