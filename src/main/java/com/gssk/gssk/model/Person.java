package com.gssk.gssk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "tbl_person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name = "birthday")
    private Date birthDay;
    @Column(name = "id_card")
    private String idCard;
    @Column(name = "email")
    private String email;
    @Column(name= "phone_number")
    private String phoneNumber;
    @Column(name= "gender")
    private String gender;

    @OneToMany
    private List<Relative> relativeList;

    @OneToOne
    private HealthRecord healthRecord;

    public List<String> To_Genome()//to return a list of string for making genome.
    // Any other customization will be modified later through another mean
    {
        String applicant="{";
        applicant+="key:"+this.id+",";
        applicant+="n:"+this.firstName+" "+this.lastName+",";
        applicant+="s:"+this.gender;
        applicant+="}";
        List<String> everyone = new List<>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String get(int index) {
                return null;
            }

            @Override
            public String set(int index, String element) {
                return null;
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        everyone.add(applicant);
        String mem;
        for (Relative member:this.relativeList
             ) {
            mem="{";
            mem+="key:"+ member.getId() +",";
            mem+="n:"+ member.getFirstName() +" "+ member.getLastName() +",";
            mem+="s:"+ member.getGender();
            mem+="}";
            everyone.add(mem);
        }
        return everyone;
    }


}
