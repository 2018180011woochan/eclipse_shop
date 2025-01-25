package com.example.shop_project_v2.point.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.example.shop_project_v2.BaseEntity;
import com.example.shop_project_v2.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@DynamicInsert
public class Point extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer balance;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @OneToMany(mappedBy = "point", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SavedPoint> savedPointList = new ArrayList<>();

    @OneToMany(mappedBy = "point", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UsedPoint> usedPointList = new ArrayList<>();

    public void usePoint(UsedPoint usedPoint){
        usedPoint.assignPointToCreate(this);
        usedPointList.add(usedPoint);
        balance -= usedPoint.getAmount();
    }

    public void savePoint(SavedPoint savedPoint){
        savedPoint.assignPointToCreate(this);
        savedPointList.add(savedPoint);
        balance += savedPoint.getSavedPoint();
    }

    public void rollbackBalance(Integer amount){
        balance -= amount;
    }
}
